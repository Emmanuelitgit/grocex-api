package com.grocex_api.authenticationService;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.models.User;
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

@Slf4j
@RestController
@RequestMapping("/api/v1/users/authenticate")
public class AuthenticationRest {

    private final JWTAccess jwtAccess;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthenticationRest(JWTAccess jwtAccess, AuthenticationManager authenticationManager) {
        this.jwtAccess = jwtAccess;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping
    public ResponseEntity<ResponseDTO> authenticateUser(@RequestBody User credentials){
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
        String token = jwtAccess.generateToken(credentials.getUsername());
        Map<String, String> tokenData = new HashMap<>();
        tokenData.put("token", token);
        tokenData.put("email", credentials.getEmail());
        log.info("Authentication success:=========");
        ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.OK, tokenData);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
