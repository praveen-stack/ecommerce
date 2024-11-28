package com.ecommerce.paymentservice.dtos;

import com.ecommerce.paymentservice.enums.CashfreeLinkStatus;
import lombok.Data;

import java.io.Serializable;

@Data
public class CashfreePaymentLinkEventData implements Serializable {
    private int cf_link_id;
    private String link_id;
    private CashfreeLinkStatus link_status;
}

