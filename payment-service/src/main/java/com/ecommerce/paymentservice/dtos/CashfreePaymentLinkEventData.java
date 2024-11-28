package com.ecommerce.paymentservice.dtos;

import com.ecommerce.paymentservice.enums.CashfreeLinkStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CashfreePaymentLinkEventData implements Serializable {
    private String cf_link_id;
    private String link_id;
    private CashfreeLinkStatus link_status;
}

