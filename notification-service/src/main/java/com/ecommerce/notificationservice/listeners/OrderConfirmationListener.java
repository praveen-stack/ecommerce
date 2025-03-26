package com.ecommerce.notificationservice.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.notificationservice.dtos.OrderDto;
import com.ecommerce.notificationservice.enums.KafkaTopics;
import com.ecommerce.notificationservice.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ecommerce.notificationservice.dtos.OrderUpdateMessage;

@Service
public class OrderConfirmationListener {

    private static final Logger logger = LoggerFactory.getLogger(OrderConfirmationListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;
    
    @KafkaListener(topics = KafkaTopics.ORDER_CONFIRMATION)
    public void listen(String message) {
        logger.info("Received ORDER_CONFIRMATION event: {}", message);        
        try {
            OrderUpdateMessage orderUpdateMessage = objectMapper.readValue(message, OrderUpdateMessage.class);
            OrderDto orderDto = orderUpdateMessage.getOrder();
            
            logger.info("Processing ORDER_CONFIRMATION for order ID: {}", orderDto.getId());
            
            // Send order confirmation email
            emailService.sendOrderConfirmationEmail(
                orderDto.getUserEmail(),
                orderDto.getId().toString(),
                orderDto.getTotalAmount()
            );
            
            logger.info("Completed processing ORDER_CONFIRMATION for order ID: {}", orderDto.getId());
        } catch (JsonProcessingException e) {
            logger.error("Error processing order confirmation message: {}", e.getMessage());
            throw new RuntimeException("Failed to process order confirmation message", e);
        }
    }
} 