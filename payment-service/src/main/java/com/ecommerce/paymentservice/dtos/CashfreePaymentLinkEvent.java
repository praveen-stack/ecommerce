package com.ecommerce.paymentservice.dtos;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashfreePaymentLinkEvent implements Serializable {
    private CashfreePaymentLinkEventData data;
    private String type;
    private String version;
    private Date event_time;
}


