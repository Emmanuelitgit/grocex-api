package com.grocex_api.paymentService.dto;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
public class PaymentResponse {
   private Map<String, String> data;
}
