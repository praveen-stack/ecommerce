package com.ecommerce.notificationservice.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.notificationservice.dtos.OrderDto;
import com.ecommerce.notificationservice.dtos.PaymentDto;
import com.ecommerce.notificationservice.enums.KafkaTopics;
import com.ecommerce.notificationservice.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ecommerce.notificationservice.dtos.OrderUpdateMessage;

@Service
public class OrderPaymentFailedListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderPaymentFailedListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;
    
    @KafkaListener(topics = KafkaTopics.ORDER_PAYMENT_FAILED)
    public void listen(String message) {
        logger.info("Received ORDER_PAYMENT_FAILED event: {}", message);        
        try {
            OrderUpdateMessage orderUpdateMessage = objectMapper.readValue(message, OrderUpdateMessage.class);
            OrderDto orderDto = orderUpdateMessage.getOrder();
            PaymentDto paymentDto = orderUpdateMessage.getPayment();
            
            logger.info("Processing ORDER_PAYMENT_FAILED for order ID: {}", orderDto.getId());
            
            // Send payment failed email
            emailService.sendPaymentFailedEmail(
                orderDto.getUserEmail(),
                orderDto.getId().toString(),
                orderDto.getStatus(),
                paymentDto.getPaymentLink()
            );
            
            logger.info("Completed processing ORDER_PAYMENT_FAILED for order ID: {}", orderDto.getId());
        } catch (JsonProcessingException e) {
            logger.error("Error processing order payment failed message: {}", e.getMessage());
            throw new RuntimeException("Failed to process order payment failed message", e);
        }
    }
} 