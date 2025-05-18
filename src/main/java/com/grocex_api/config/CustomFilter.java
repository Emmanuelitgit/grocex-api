package com.grocex_api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.grocex_api.authenticationService.JWTAccess;
import com.grocex_api.exception.UnAuthorizeException;
import com.grocex_api.utils.AppUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.james.mime4j.field.datetime.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.sql.Time;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomFilter extends OncePerRequestFilter {

    private final JWTAccess jwtAccess;
    private final AppUtils appUtils;

    @Autowired
    public CustomFilter(JWTAccess jwtAccess, AppUtils appUtils) {
        this.jwtAccess = jwtAccess;
        this.appUtils = appUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
       try {
           String auth = request.getHeader("Authorization");
           if (auth!=null){
               String token = auth.substring(7);
               jwtAccess.isTokenValid(token);
               String username = jwtAccess.extractUsername(token);
               appUtils.setAuthorities(username);
           }
           filterChain.doFilter(request, response);

       }catch (UnAuthorizeException ex) {
           response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
           response.setContentType("application/json");
           Map<String, Object> res = new HashMap<>();
           res.put("message", ex.getMessage());
           res.put("statusCode", HttpStatus.valueOf(401));
           res.put("date", Time.from(Instant.now()));
           ObjectMapper mapper = new ObjectMapper();
           String responseData = mapper.writeValueAsString(res);
           response.getWriter().write(responseData);
       }
    }
}
