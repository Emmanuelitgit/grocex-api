package com.grocex_api.notificationService.serviceImpl;

import com.grocex_api.exception.BadRequestException;
import com.grocex_api.exception.NotFoundException;
import com.grocex_api.exception.ServerException;
import com.grocex_api.notificationService.dto.OrderNotificationPayload;
import com.grocex_api.notificationService.models.OTP;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.repo.UserRepo;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Slf4j
@Service
public class OrderNotificationServiceImpl {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserRepo userRepo;

    @Autowired
    public OrderNotificationServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, UserRepo userRepo) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userRepo = userRepo;
    }

    public void SendToCustomer(OrderNotificationPayload payload){
        try {
            log.info("In order notification method:->>>>>>");
            User user = userRepo.findUserByEmail(payload.getEmail())
                    .orElseThrow(()-> new NotFoundException("user record not found to send email"));

            // setting email items
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("Order Summary");
            helper.setFrom("eyidana001@gmail.com");
            helper.setTo(payload.getEmail());

            // setting variables values to passed to the template
            Context context = new Context();
            context.setVariable("fullName", user.getFirstName() + " " + user.getLastName());
            context.setVariable("products", payload.getProducts());
            context.setVariable("totals", payload.getOrderTotals());

            String htmlContent = templateEngine.process("OrderTemplate", context);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Order notification sent:->>>>>>>>");

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            throw new ServerException("Internal server error!");
        }
    }

    public void SendToClient(OrderNotificationPayload payload){
    }
}
