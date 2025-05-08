package com.grocex_api.config;

import com.grocex_api.authenticationService.JWTAccess;
import com.grocex_api.utils.AppUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
//        String auth = request.getHeader("Authorization");
//        if (auth.startsWith("Bearer") && auth!=null){
//            String token = auth.substring(7);
//            jwtAccess.isTokenValid(token);
//            String username = jwtAccess.extractUsername(token);
//            log.info("Username:->>>{}", username);
////        appUtils.setAuthorities(username);
////            log.info("Header:->>>{}",token);
//        }
        filterChain.doFilter(request, response);
    }
}
