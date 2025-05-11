package com.grocex_api.notificationService.serviceImpl;

import com.grocex_api.exception.BadRequestException;
import com.grocex_api.exception.NotFoundException;
import com.grocex_api.exception.ServerException;
import com.grocex_api.notificationService.dto.OTPPayload;
import com.grocex_api.notificationService.models.OTP;
import com.grocex_api.notificationService.repo.OTPRepo;
import com.grocex_api.notificationService.service.OTPService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.utils.AppUtils;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.Random;
import java.util.random.RandomGenerator;

@Slf4j
@Service
public class OTPServiceImpl implements OTPService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final UserRepo userRepo;
    private final OTPRepo otpRepo;

    @Autowired
    public OTPServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine, UserRepo userRepo, OTPRepo otpRepo) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
    }

    /**
     * @description This method is used to send an otp code to a user given the required payload.
     * @param otpPayload
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 10h May 2025
     */
    @Override
    public void sendOtp(OTPPayload otpPayload) {
        try {
            log.info("In send otp method:->>>>>>");
            User user = userRepo.findUserByEmail(otpPayload.getEmail())
                    .orElseThrow(()-> new NotFoundException("user record not found to send email"));

            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setSubject("OTP Verification");
            helper.setFrom("eyidana001@gmail.com");
            helper.setTo(otpPayload.getEmail());

            Context context = new Context();
            otpPayload.setOtpCode(generateOTP());
            context.setVariable("otp", otpPayload.getOtpCode());
            context.setVariable("fullName", user.getFirstName() + " " + user.getLastName());

            String htmlContent = templateEngine.process("OTPTemplate", context);
            helper.setText(htmlContent, true);

            OTP otp = saveOTP(otpPayload);
            if (otp == null){
                throw new BadRequestException("fail to save otp record");
            }
            log.info("Otp sent:->>>>>>>>");
            mailSender.send(message);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            throw new ServerException("Internal server error!");
        }
    }

    public OTP saveOTP(OTPPayload otpPayload){
        User user = userRepo.findUserByEmail(otpPayload.getEmail())
                .orElseThrow(()-> new NotFoundException("user record not found to send email"));
        OTP otp = new OTP();
        otp.setOtpCode(otpPayload.getOtpCode());
        otp.setStatus(false);
        otp.setExpireAt(ZonedDateTime.now().plusMinutes(2));
        otp.setUserId(user.getId());
        return otpRepo.save(otp);
    }

    /**
     * @description This method is used to verify user otp.
     * @param otpPayload
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 10h May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> verifyOtp(OTPPayload otpPayload) {
        User user = userRepo.findUserByEmail(otpPayload.getEmail())
                .orElseThrow(()-> new NotFoundException("user record not found!"));

        OTP otpExist = otpRepo.findByUserId(user.getId());
        if (otpExist == null){
            ResponseDTO response = AppUtils.getResponseDto("OTP record not found", HttpStatus.NOT_FOUND);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }

        if (!ZonedDateTime.now().isBefore(otpExist.getExpireAt())){
            ResponseDTO response = AppUtils.getResponseDto("OTP has expired", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        if (otpPayload.getOtpCode() != otpExist.getOtpCode()){
            ResponseDTO response = AppUtils.getResponseDto("OTP do not match", HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        otpExist.setStatus(true);
        otpRepo.deleteById(otpExist.getId());

        ResponseDTO response = AppUtils.getResponseDto("OTP verified", HttpStatus.OK);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public Integer generateOTP(){
        RandomGenerator generator = new Random();
        return generator.nextInt(2001, 9000);
    }
}
