package com.ecommerce.paymentservice.services;

import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.KafkaTopics;
import com.ecommerce.paymentservice.enums.PaymentMethod;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.exceptions.NotFoundException;
import com.ecommerce.paymentservice.models.Payment;
import com.ecommerce.paymentservice.paymentgateways.PaymentGatewayFactory;
import com.ecommerce.paymentservice.repositories.PaymentRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private PaymentGatewayFactory paymentGatewayFactory;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Payment getActivePayment(Long orderId) {
        return paymentRepository.findByOrderIdAndStatus(orderId, PaymentStatus.PENDING);
    }

    @Override
    @Transactional
    public Payment createPayment(AuthorizedUser user, Long orderId, Double amount, PaymentMethod paymentMethod) {
        var payment = getActivePayment(orderId);
        if (payment != null) {
            return payment;
        }
        var paymentGateway = paymentGatewayFactory.getPaymentGateway();
        PaymentLink link = paymentGateway.createPaymentLink(orderId, user, amount, paymentMethod);
        payment = new Payment();
        payment.setPaymentMethod(paymentMethod);
        payment.setGatewayPaymentId(link.getId());
        payment.setPaymentLink(link.getLink());
        payment.setOrderId(orderId);
        payment.setUserId(user.getId());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setGateway(paymentGateway.getGateway());
        return paymentRepository.save(payment);
    }

    @Override
    public Payment getPaymentById(AuthorizedUser user, Long paymentId) {
        var payment = paymentRepository.findByIdAndUserId(paymentId, user.getId());
        if (payment == null) {
            throw new NotFoundException("Payment not found");
        }
        return payment;
    }

    private void sendPaymentUpdateEvent(Payment payment) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        String message = objectMapper.writeValueAsString(payment);
        kafkaTemplate.send(KafkaTopics.PAYMENT_UPDATE, message);
    }

    @Override
    @Transactional
    public Payment updatePaymentStatus(String gatewayPaymentId, PaymentStatus status) throws JsonProcessingException {
        var payment = paymentRepository.findByGatewayPaymentId(gatewayPaymentId).orElseThrow(() -> new NotFoundException("Payment not found"));
        payment.setStatus(status);
        payment = paymentRepository.save(payment);
        this.sendPaymentUpdateEvent(payment);
        return payment;
    }
}
