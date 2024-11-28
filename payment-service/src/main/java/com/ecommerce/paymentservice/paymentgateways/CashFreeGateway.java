package com.ecommerce.paymentservice.paymentgateways;

import com.cashfree.ApiException;
import com.cashfree.ApiResponse;
import com.cashfree.Cashfree;
import com.cashfree.model.CreateLinkRequest;
import com.cashfree.model.LinkCustomerDetailsEntity;
import com.cashfree.model.LinkEntity;
import com.cashfree.model.LinkNotifyEntity;
import com.ecommerce.paymentservice.dtos.CashfreePaymentLinkEvent;
import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.enums.KafkaTopics;
import com.ecommerce.paymentservice.enums.PaymentStatus;
import com.ecommerce.paymentservice.exceptions.PaymentGatewayException;
import com.ecommerce.paymentservice.services.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CashFreeGateway implements PaymentGateway {

    private static final Logger logger = LoggerFactory.getLogger(CashFreeGateway.class);

    @Autowired
    private Cashfree cashfree;

    @Autowired
    private PaymentService paymentService;

    @Override
    public PaymentLink createPaymentLink(Long orderId, Long userId, Double amount) {
        String xApiVersion = "2022-09-01";
        String orderIdStr = orderId.toString();
        UUID uuid = UUID.randomUUID();
        CreateLinkRequest createLinkRequest = new CreateLinkRequest();
        createLinkRequest.setLinkId(uuid.toString());
        createLinkRequest.setLinkAmount(amount);
        createLinkRequest.setLinkPurpose("Payment for order " + orderIdStr);
        createLinkRequest.setLinkCurrency("INR");
        LinkNotifyEntity notifyEntity = new LinkNotifyEntity();
        notifyEntity.setSendEmail(false);
        notifyEntity.setSendSms(false);
        createLinkRequest.setLinkNotify(notifyEntity);
        LinkCustomerDetailsEntity customerDetailsEntity = new LinkCustomerDetailsEntity();
        customerDetailsEntity.setCustomerEmail("testemail@test.com");
        createLinkRequest.setCustomerDetails(customerDetailsEntity);

        try {
            ApiResponse<LinkEntity> response = cashfree.PGCreateLink(xApiVersion, createLinkRequest, null, null, null);
            PaymentLink paymentLink = new PaymentLink();
            paymentLink.setLink(response.getData().getLinkUrl());
            paymentLink.setId(response.getData().getLinkId());
            return paymentLink;
        } catch (ApiException e) {
            throw new PaymentGatewayException(e.getMessage());
        }
    }

    @Override
    public Gateway getGateway() {
        return Gateway.CASHFREE;
    }

    @KafkaListener(topics = KafkaTopics.CASHFREE_PAYMENTS)
    public void listen(String message) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        CashfreePaymentLinkEvent event = objectMapper.readValue(message, CashfreePaymentLinkEvent.class);
        var linkStatus = event.getData().getLink_status();
        var eventData = event.getData();
        logger.info("Received payment link event for link: {} with status: {}", eventData.getLink_id(), linkStatus);
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
