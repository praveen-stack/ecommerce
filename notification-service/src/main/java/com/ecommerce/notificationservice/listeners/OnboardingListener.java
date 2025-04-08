package com.ecommerce.notificationservice.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import com.ecommerce.notificationservice.enums.KafkaTopics;
import com.ecommerce.notificationservice.services.EmailService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.ecommerce.notificationservice.dtos.UserDto;

@Service
public class OnboardingListener {

    private static final Logger logger = LoggerFactory.getLogger(OnboardingListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EmailService emailService;
    
    @KafkaListener(topics = KafkaTopics.USER_ONBOARDING)
    public void listen(String message) {
        logger.info("Received USER_ONBOARDING event: {}", message);        
        try {
            UserDto user = objectMapper.readValue(message, UserDto.class);
            
            logger.info("Processing USER_ONBOARDING for user ID: {}", user.getId());
            emailService.sendOnboardingEmail(
                user.getEmail(),
                user.getName()
            );
            
            logger.info("Completed processing USER_ONBOARDING for user ID: {}", user.getId());
        } catch (JsonProcessingException e) {
            logger.error("Error processing user onboarding message: {}", e.getMessage());
            throw new RuntimeException("Failed to process user onboarding message", e);
        }
    }
} 