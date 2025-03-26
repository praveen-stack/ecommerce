package com.ecommerce.notificationservice.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.ecommerce.notificationservice.dtos.PasswordResetMessage;
import com.ecommerce.notificationservice.enums.KafkaTopics;
import com.ecommerce.notificationservice.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class PasswordResetListener {

    private static final Logger logger = LoggerFactory.getLogger(PasswordResetListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired EmailService emailService;
    
    @KafkaListener(topics = KafkaTopics.PASSWORD_RESET_REQUEST)
    public void listen(String message) {
        logger.info("Received PASSWORD_RESET_REQUEST event: {}", message);        
        try {
            PasswordResetMessage resetMessage = objectMapper.readValue(message, PasswordResetMessage.class);                
            logger.info("Processing PASSWORD_RESET_REQUEST for user email: {}", resetMessage.getEmail());
            emailService.sendPasswordResetEmail(resetMessage.getEmail(), resetMessage.getToken());
            logger.info("Completed processing PASSWORD_RESET_REQUEST for user email: {}", resetMessage.getEmail());
        } catch (JsonProcessingException e) {
            logger.error("Error processing password reset message: {}", e.getMessage());
            throw new RuntimeException("Failed to process password reset message", e);
        }
    }

    
}
