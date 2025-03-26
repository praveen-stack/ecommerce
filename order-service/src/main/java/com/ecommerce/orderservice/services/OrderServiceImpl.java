package com.ecommerce.orderservice.services;

import com.ecommerce.orderservice.Repositories.OrderRepository;
import com.ecommerce.orderservice.dtos.AuthorizedUser;
import com.ecommerce.orderservice.dtos.CreatePaymentDto;
import com.ecommerce.orderservice.dtos.OrderDto;
import com.ecommerce.orderservice.dtos.OrderUpdateMessage;
import com.ecommerce.orderservice.dtos.Payment;
import com.ecommerce.orderservice.dtos.Product;
import com.ecommerce.orderservice.enums.KafkaTopics;
import com.ecommerce.orderservice.enums.OrderStatus;
import com.ecommerce.orderservice.enums.PaymentMethod;
import com.ecommerce.orderservice.exceptions.NotFoundException;
import com.ecommerce.orderservice.models.Address;
import com.ecommerce.orderservice.models.Order;
import com.ecommerce.orderservice.utils.OrderHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.kafka.core.KafkaTemplate;

@Service
public class OrderServiceImpl implements OrderService {

    private static final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductCatalogService productCatalogService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Override
    @Transactional
    public Order createOrder(AuthorizedUser user, Order order, Long billingAddressId, Long shippingAddressId,
            PaymentMethod paymentMethod) {

        Set<Long> productIds = order.getItems().stream().map(i -> i.getProductId()).collect(Collectors.toSet());
        List<Product> products = productCatalogService.getProductsByIds(productIds.stream().toList(), user.getJwt());
        Map<Long, Product> productsMap = new HashMap<>();
        for (var product : products) {
            productsMap.put(product.getId(), product);
        }

        Address billingAddress = userService.getAddressById(billingAddressId, user);
        Address shippingAddress = userService.getAddressById(shippingAddressId, user);
        order.setBillingAddress(billingAddress);
        order.setShippingAddress(shippingAddress);
        order.setStatus(OrderStatus.PENDING);
        order.setUserId(user.getId());
        double orderTotal = 0d;
        for (var item : order.getItems()) {
            double itemTotal = item.getQuantity() * item.getPrice();
            var product = productsMap.get(item.getProductId());
            item.setTotalAmount(itemTotal);
            item.setProductDescription(product.getDescription());
            item.setProductImage(product.getImage());
            item.setProductTitle(product.getTitle());
            item.setOrder(order);
            orderTotal += itemTotal;
        }
        order.setTotalAmount(orderTotal);
        order.setUserEmail(user.getEmail());
        order = orderRepository.save(order);
        CreatePaymentDto createPaymentDto = new CreatePaymentDto();
        createPaymentDto.setPaymentMethod(paymentMethod);
        createPaymentDto.setAmount(order.getTotalAmount());
        createPaymentDto.setOrderId(order.getId());
        Payment payment = paymentService.createPayment(user, createPaymentDto);
        order.setPaymentId(payment.getId());
        orderRepository.save(order);
        OrderUpdateMessage orderUpdateMessage = createOrderUpdateMessage(order, payment);
        try {
            kafkaTemplate.send(KafkaTopics.ORDER_CONFIRMATION, objectMapper.writeValueAsString(orderUpdateMessage));
        } catch (Exception e) {
            logger.error("Error sending order confirmation message: {}", e.getMessage());
        }
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public Order getOrder(AuthorizedUser user, Long orderId) {
        var orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            throw new NotFoundException("Order not found");
        }
        var order = orderOptional.get();
        if (!order.getUserId().equals(user.getId())) {
            throw new NotFoundException("Order not found");
        }
        return order;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> getAllOrders(AuthorizedUser user) {
        return orderRepository.findByUserId(user.getId());
    }

    @KafkaListener(topics = KafkaTopics.PAYMENT_UPDATE)
    public void listen(String message) {
        logger.info("Received payment update event: {}", message);
        try {
            Payment payment = objectMapper.readValue(message, Payment.class);
            logger.info("Processing payment update for paymentId: {} with status: {}", payment.getId(),
                    payment.getStatus());
            // update order status
            var orderOptional = orderRepository.findById(payment.getOrderId());
            if (orderOptional.isEmpty()) {
                throw new NotFoundException("Order not found");
            }
            var order = orderOptional.get();
            OrderUpdateMessage orderUpdateMessage;
            switch (payment.getStatus()) {
                case SUCCESS:
                    order.setStatus(OrderStatus.CONFIRMED);
                    orderRepository.save(order);
                    orderUpdateMessage = createOrderUpdateMessage(order, payment);
                    kafkaTemplate.send(KafkaTopics.ORDER_PROCESSING,
                            objectMapper.writeValueAsString(orderUpdateMessage));

                    break;
                case FAILED:
                    order.setStatus(OrderStatus.PAYMENT_FAILED);
                    orderRepository.save(order);
                    orderUpdateMessage = createOrderUpdateMessage(order, payment);
                    kafkaTemplate.send(KafkaTopics.ORDER_PAYMENT_FAILED,
                            objectMapper.writeValueAsString(orderUpdateMessage));

                    break;
                default:
                    break;
            }
            logger.info("Order status updated to: {}", order.getStatus());
        } catch (Exception e) {
            logger.error("Error sending order payment failed message: {}", e.getMessage());
        }
    }

    private OrderUpdateMessage createOrderUpdateMessage(Order order, Payment payment) {
        OrderUpdateMessage orderUpdateMessage = new OrderUpdateMessage();
        OrderDto orderDto = OrderHelper.toDto(order);
        orderUpdateMessage.setOrder(orderDto);
        orderUpdateMessage.setPayment(payment);
        return orderUpdateMessage;
    }

}
