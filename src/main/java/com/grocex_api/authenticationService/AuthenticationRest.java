package com.grocex_api.authenticationService;

import com.grocex_api.authenticationService.dto.Credentials;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserDTOProjection;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/v1/users/authenticate")
public class AuthenticationRest {

    private final JWTAccess jwtAccess;
    private final AuthenticationManager authenticationManager;
    private final UserRepo userRepo;

    @Autowired
    public AuthenticationRest(JWTAccess jwtAccess, AuthenticationManager authenticationManager, UserRepo userRepo) {
        this.jwtAccess = jwtAccess;
        this.authenticationManager = authenticationManager;
        this.userRepo = userRepo;
    }

    /**
     * @description This method is used to authenticate users abd generate token on authentication success.
     * @param credentials
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 30th April 2025
     */
    @PostMapping
    public ResponseEntity<ResponseDTO> authenticateUser(@RequestBody Credentials credentials){
        log.info("In authentication method:=========");
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(
                  credentials.getEmail(),
                  credentials.getPassword()
          )
        );

        if (!authentication.isAuthenticated()){
            log.info("Authentication fail:=========");
            ResponseDTO  response = AppUtils.getResponseDto("invalid credentials", HttpStatus.UNAUTHORIZED);
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
        UserDTOProjection user = userRepo.getUsersDetailsByUserEmail(credentials.getEmail());

        String token = jwtAccess.generateToken(credentials.getEmail(), user.getId());
        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("username", user.getUsername());
        tokenData.put("email", credentials.getEmail());
        tokenData.put("role", user.getRole());
        tokenData.put("full name", AppUtils.getFullName(user.getFirstName(), user.getLastName()));
        tokenData.put("token", token);
        log.info("Authentication success:=========");
        ResponseDTO  response = AppUtils.getResponseDto("authentication successfully", HttpStatus.OK, tokenData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
