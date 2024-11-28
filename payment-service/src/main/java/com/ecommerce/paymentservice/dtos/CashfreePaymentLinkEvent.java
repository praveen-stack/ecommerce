package com.ecommerce.paymentservice.dtos;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class CashfreePaymentLinkEvent implements Serializable {
    private CashfreePaymentLinkEventData data;
    private String type;
    private int version;
    private Date event_time;

}


