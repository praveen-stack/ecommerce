package com.ecommerce.paymentservice.paymentgateways;

import com.ecommerce.paymentservice.dtos.CashfreePaymentLinkEvent;
import com.ecommerce.paymentservice.enums.KafkaTopics;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class CashFreePaymentsListener {

    private static final Logger logger = LoggerFactory.getLogger(CashFreePaymentsListener.class);

    @Autowired
    private PaymentService paymentService;
    @KafkaListener(topics = KafkaTopics.CASHFREE_PAYMENTS)
    public void listen(String message) throws JsonProcessingException {
        logger.info("Received payment link event {}", message);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode messageNode = objectMapper.readTree(message);
        String body = messageNode.get("body").asText();
        logger.info(body);
        CashfreePaymentLinkEvent event = objectMapper.readValue(body, CashfreePaymentLinkEvent.class);
        var eventData = event.getData();
        var linkStatus = event.getData().getLink_status();
        logger.info("Processing link event for link: {} with status: {}", eventData.getLink_id(), linkStatus);
        PaymentStatus paymentStatus;
        switch (linkStatus) {
            case PAID:
                paymentStatus = PaymentStatus.SUCCESS;
                break;
            default:
                paymentStatus = PaymentStatus.FAILED;
        }
        paymentService.updatePaymentStatus(eventData.getLink_id(), paymentStatus);
    }
}
