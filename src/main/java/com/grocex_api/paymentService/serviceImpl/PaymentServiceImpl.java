package com.grocex_api.paymentService.serviceImpl;

import com.grocex_api.paymentService.dto.PaymentPayload;
import com.grocex_api.paymentService.dto.PaymentResponse;
import com.grocex_api.paymentService.dto.PaymentStatus;
import com.grocex_api.paymentService.models.Payment;
import com.grocex_api.paymentService.repo.PaymentRepo;
import com.grocex_api.paymentService.service.PaymentService;
import com.grocex_api.productService.dto.ProductProjection;
import com.grocex_api.productService.models.Product;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.*;

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

    /**
     * @description This method is used to initialize payment process.
     * @param paymentPayload
     * @param response
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 3rd may 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> initializePayment(PaymentPayload paymentPayload, HttpServletResponse response) {

        try{
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(PAYMENT_TOKEN);

            HttpEntity entity = new HttpEntity(paymentPayload, headers);
            ResponseEntity<PaymentResponse> paystackResponse = restTemplate.postForEntity(INITIALIZE_PAYMENT, entity, PaymentResponse.class);
            Payment payment  = new Payment();
            if (paystackResponse.getStatusCode() == HttpStatusCode.valueOf(200)){
                payment.setReference(paystackResponse .getBody().getData().get("reference"));
                payment.setAccessCode(paystackResponse .getBody().getData().get("access_code"));
                payment.setAuthorizationUrl(paystackResponse .getBody().getData().get("authorization_url"));
                payment.setOrderId(paymentPayload.getOrderId());
                payment.setAmount(payment.getAmount());
                Payment paymentRes = paymentRepo.save(payment);
                response.sendRedirect(paymentRes.getAuthorizationUrl());
            }

            log.info("payment initialization:->>>{}", paystackResponse.getBody());
            ResponseDTO  res = AppUtils.getResponseDto("payment initialization success", HttpStatus.valueOf(paystackResponse.getStatusCode().value()));
            return new ResponseEntity<>(res, HttpStatus.valueOf(paystackResponse.getStatusCode().value()));
        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  res = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to fetch all payments records.
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 3rd may 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        try{
            List<Payment> paymentsRest = paymentRepo.findAll();
            if (paymentsRest.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no payment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("payments records fetched successfully successfully", HttpStatus.OK, paymentsRest);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to initialize payment records given the payment id.
     * @param paymentId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 3rd may 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findPaymentById(UUID paymentId) {
        try{
            log.info("In get product by id method:->>>>>>");
            Optional<Payment> paymentOptional = paymentRepo.findById(paymentId);
            if (paymentOptional.isEmpty()){
                log.error("no payment record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no payment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            ResponseDTO  response = AppUtils.getResponseDto("payment records fetched successfully", HttpStatus.OK, paymentOptional.get());
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to initialize payment records.
     * @param payment
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 3rd may 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updatePayment(Payment payment) {
        try {
            log.info("In update payment method:->>>>>>");
            Optional<Payment> paymentOptional = paymentRepo.findById(payment.getId());
            if (paymentOptional.isEmpty()){
                log.error("no payment record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no payment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            Payment existingData = paymentOptional.get();
            existingData.setAmount(payment.getAmount());
            existingData.setOrderId(payment.getOrderId());

            Payment paymentRes = paymentRepo.save(existingData);

            ResponseDTO  response = AppUtils.getResponseDto("payment records updated successfully", HttpStatus.OK, paymentRes);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to remove payment record given the id.
     * @param paymentId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 3rd may 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removePayment(UUID paymentId) {
        try {
            log.info("In remove payment method:->>>>>>{}", paymentId);
            Optional<Payment> paymentOptional = paymentRepo.findById(paymentId);
            if (paymentOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no payment record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            paymentRepo.deleteById(paymentId);
            log.info("payment removed successfully:->>>>>>");
            ResponseDTO  response = AppUtils.getResponseDto("payment record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description a method for verifying transaction status through payStack webHook
     * @param data
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 3rd May 2025
     */
    @Override
    public void processSuccessfulPayment(Map<String, Object> data) {
        String currency = (String) data.get("currency");
        String status = (String) data.get("status");
        String paidAt = (String) data.get("paid_at");
        String channel = (String) data.get("channel");
        Map<String, Object> metadata = (Map<String, Object>) data.get("metadata");
        String rentInfoIdStr = (String) metadata.get("rentInfoId"); // Get the value as a String

        Payment payment = new Payment();
        payment.setStatus(PaymentStatus.PAID);
        payment.setChannel(channel);
        payment.setCurrency(currency);
        payment.setPaidAt(ZonedDateTime.parse(paidAt));


    }
}
