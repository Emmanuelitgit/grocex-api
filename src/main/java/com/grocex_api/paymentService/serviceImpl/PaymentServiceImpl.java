package com.grocex_api.paymentService.serviceImpl;

import com.grocex_api.paymentService.dto.PaymentResponse;
import com.grocex_api.paymentService.models.Payment;
import com.grocex_api.paymentService.repo.PaymentRepo;
import com.grocex_api.paymentService.service.PaymentService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final String INITIALIZE_PAYMENT = "https://api.paystack.co/transaction/initialize";
    private final String VERIFY_PAYMENT = "https://api.paystack.co/transaction/verify";
    private final String PAYMENT_TOKEN = "sk_test_3d82c476abd2aec8d0cda1f5d84cb42fb545ac82";

    private final RestTemplate restTemplate;
    private final PaymentRepo paymentRepo;

    @Autowired
    public PaymentServiceImpl(RestTemplate restTemplate, PaymentRepo paymentRepo) {
        this.restTemplate = restTemplate;
        this.paymentRepo = paymentRepo;
    }

    @Override
    public ResponseEntity<ResponseDTO> savePayment(Payment payment) {

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(PAYMENT_TOKEN);

            HttpEntity entity = new HttpEntity(payment, headers);

            ResponseEntity<PaymentResponse> paystackResponse = restTemplate.postForEntity(INITIALIZE_PAYMENT, entity, PaymentResponse.class);
            if (paystackResponse .getStatusCode() == HttpStatusCode.valueOf(200)){
                payment.setReference(paystackResponse .getBody().getReference());
                payment.setAccessCode(paystackResponse .getBody().getAccess_code());
                payment.setAuthorizationUrl(paystackResponse .getBody().getAuthorization_url());
            }

            Payment paymentRes = paymentRepo.save(payment);
            ResponseDTO  response = AppUtils.getResponseDto("payment initialized successfully", HttpStatus.OK, paymentRes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            List<Payment> paymentsRest = paymentRepo.findAll();
            if (paymentsRest.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no payment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("payment initialized successfully", HttpStatus.OK, paymentsRest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> findPaymentById(UUID paymentId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updatePayment(Payment payment) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> removePayment(UUID paymentId) {
        return null;
    }
}
