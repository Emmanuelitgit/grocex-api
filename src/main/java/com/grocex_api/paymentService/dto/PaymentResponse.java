package com.grocex_api.paymentService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

@Component
@Data
public class PaymentResponse {
    private String authorization_url;
    private String access_code;
    private String reference;
}
