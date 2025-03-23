package com.ecommerce.cartservice.services;

import com.ecommerce.cartservice.configuration.AppConfig;
import com.ecommerce.cartservice.dtos.*;
import com.ecommerce.cartservice.exceptions.BadRequestException;
import com.ecommerce.cartservice.models.Cart;
import com.ecommerce.cartservice.models.Item;
import com.ecommerce.cartservice.repositories.CartRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;


    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    private static final String CART_CACHE_KEY = "#user.id";

    @Autowired
    private ProductCatalogService productCatalogService;

    @Override
    @Transactional
    @CachePut(value = "cartCache", key = CART_CACHE_KEY)
    public Cart addToCart(AuthorizedUser user, Long productId, Integer quantity) {
        var userId = user.getId();
        Cart cart = getCartByUserId(userId);
        cart.addToCart(productId, quantity);
        cart = cartRepository.save(cart);
        refreshCartDetails(cart, user);
        return cart;
    }

    private Cart refreshCartDetails(Cart cart, AuthorizedUser user) {
        List<Long> productIds = cart.getItems().stream().map(Item::getProductId).toList();
        List<Product> products = productCatalogService.getProductsByIds(productIds, user.getJwt());
        var productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
        cart.getItems().forEach(item -> {
            var product = productMap.get(item.getProductId());
            item.setProduct(product);
            var itemTotal = product.getPrice() * item.getQuantity();
            item.setItemTotal(itemTotal);
        });
        var total = cart.getItems().stream().mapToDouble(Item::getItemTotal).sum();
        cart.setTotal(total);
        return cart;
    }

    @Override
    @Transactional
    @CachePut(value = "cartCache", key = CART_CACHE_KEY)
    public Cart removeFromCart(AuthorizedUser user, Long productId, Optional<Integer> quantityOptional) {
        var userId = user.getId();
        Cart cart = getCartByUserId(userId);
        cart.remove(productId, quantityOptional);
        cart = cartRepository.save(cart);
        refreshCartDetails(cart, user);
        return cart;
    }

    private Cart initializeCart(Long userId) {
        return cartRepository.save(new Cart(userId));
    }

    public Cart getCartByUserId(Long userId) {
        var cartOptional = cartRepository.findByUserId(userId);
        Cart cart;
        if(cartOptional.isEmpty()){
            cart = initializeCart(userId);
        } else{
            cart = cartOptional.get();
        }
        return cart;
    }

    @Override
    @Cacheable(value = "cartCache", key = CART_CACHE_KEY)
    public Cart getCart(AuthorizedUser user) {
        var userId = user.getId();
        Cart cart = getCartByUserId(userId);
        refreshCartDetails(cart, user);
        return cart;
    }

    @Override
    @Transactional
    public CheckoutResponseDto checkout(AuthorizedUser user, CheckoutRequestDto checkoutRequest) {
        // Get the current cart
        Cart cart = getCart(user);
        
        if (cart.getItems().isEmpty()) {
            throw new BadRequestException("Cart is empty");
        }

        // Create order request
        CreateOrderDto createOrderRequest = new CreateOrderDto();
        createOrderRequest.setBillingAddressId(checkoutRequest.getBillingAddressId());
        createOrderRequest.setShippingAddressId(checkoutRequest.getShippingAddressId());
        createOrderRequest.setPaymentMethod(checkoutRequest.getPaymentMethod());
        createOrderRequest.setItems(cart.getItems().stream().map(item -> {
            CreateOrderItemDto orderItem = new CreateOrderItemDto();
            orderItem.setProductId(item.getProductId());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setPrice(item.getProduct().getPrice());
            return orderItem;
        }).toList());

        // Create order using OrderService
        OrderDto order = orderService.createOrder(user, createOrderRequest);

        // Clear the cart after successful order creation
        cartRepository.delete(cart);

        // Create and return checkout response
        CheckoutResponseDto response = new CheckoutResponseDto();
        response.setOrder(order);
        
        // Get payment details using the payment ID from the order
        if (order.getPaymentId() != null) {
            PaymentDto payment = paymentService.getPaymentById(user, order.getPaymentId());
            response.setPayment(payment);
        }
        
        return response;
    }
}
