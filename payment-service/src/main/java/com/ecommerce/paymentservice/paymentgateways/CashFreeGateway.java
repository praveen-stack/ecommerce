package com.ecommerce.paymentservice.paymentgateways;

import com.cashfree.ApiException;
import com.cashfree.ApiResponse;
import com.cashfree.Cashfree;
import com.cashfree.model.*;
import com.ecommerce.paymentservice.dtos.AuthorizedUser;
import com.ecommerce.paymentservice.dtos.PaymentLink;
import com.ecommerce.paymentservice.enums.Gateway;
import com.ecommerce.paymentservice.enums.PaymentMethod;
import com.ecommerce.paymentservice.exceptions.PaymentGatewayException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CashFreeGateway implements PaymentGateway {

    @Autowired
    private Cashfree cashfree;

    private String getPaymentMethodSting(PaymentMethod paymentMethod) {
        switch (paymentMethod) {
            case UPI:
                return "upi";
            case CREDIT_CARD:
                return "cc";
            case NET_BANKING:
                return "nb";
            case DEBIT_CARD:
                return "dc";
            default:
                throw new RuntimeException("Unknown payment method");
        }
    }

    @Override
    public PaymentLink createPaymentLink(Long orderId, AuthorizedUser user, Double amount, PaymentMethod paymentMethod) {
        String xApiVersion = "2022-09-01";
        String orderIdStr = orderId.toString();
        UUID uuid = UUID.randomUUID();
        CreateLinkRequest createLinkRequest = new CreateLinkRequest();
        createLinkRequest.setLinkId(uuid.toString());
        createLinkRequest.setLinkAmount(amount);
        createLinkRequest.setLinkPurpose("Payment for order " + orderIdStr);
        createLinkRequest.setLinkCurrency("INR");
        if(paymentMethod != null){
            LinkMetaResponseEntity linkMeta = new LinkMetaResponseEntity();
            linkMeta.setPaymentMethods(this.getPaymentMethodSting(paymentMethod));
            createLinkRequest.setLinkMeta(linkMeta);
        }

        LinkNotifyEntity notifyEntity = new LinkNotifyEntity();
        notifyEntity.setSendEmail(false);
        notifyEntity.setSendSms(false);
        createLinkRequest.setLinkNotify(notifyEntity);
        LinkCustomerDetailsEntity customerDetailsEntity = new LinkCustomerDetailsEntity();
        customerDetailsEntity.setCustomerEmail(user.getEmail());
        customerDetailsEntity.setCustomerPhone(user.getPhoneNumber());
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

}
