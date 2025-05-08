package com.grocex_api.utils;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.UserDTOProjection;
import com.grocex_api.userService.models.RoleSetup;
import com.grocex_api.userService.repo.RoleSetupRepo;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.userService.repo.UserRoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collection;

@Component
public class AppUtils {

    private final RoleSetupRepo roleSetupRepo;
    private final UserRoleRepo userRoleRepo;
    private final UserRepo userRepo;

    @Autowired
    public AppUtils(RoleSetupRepo roleSetupRepo, UserRoleRepo userRoleRepo, UserRepo userRepo) {
        this.roleSetupRepo = roleSetupRepo;
        this.userRoleRepo = userRoleRepo;
        this.userRepo = userRepo;
    }

    /**
     * This method is used to handle all responses in the application.
     * @param message
     * @param status
     * @return responseDto object
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    public static ResponseDTO getResponseDto(String message, HttpStatus status){
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setMessage(message);
        responseDto.setDate(ZonedDateTime.now());
        responseDto.setStatusCode(status.value());
        return responseDto;
    }

    /**
     * This method is used to handle all responses in the application.
     * @param message
     * @param status
     * @param data
     * @return responseDto object
     * @auther Emmanuel Yidana
     * @createdAt 16h April 2025
     */
    public static ResponseDTO getResponseDto(String message, HttpStatus status, Object data){
        if(data==null){
            ResponseDTO responseDto = getResponseDto(message, status);
            return responseDto;
        }
        ResponseDTO responseDto = new ResponseDTO();
        responseDto.setMessage(message);
        responseDto.setDate(ZonedDateTime.now());
        responseDto.setStatusCode(status.value());
        responseDto.setData(data);
        return responseDto;
    }

    public void setAuthorities(String username){
        UserDTOProjection role = userRepo.getUserRole(username);
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getRole());
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        grantedAuthorities.add(authority);
        Authentication authentication = new UsernamePasswordAuthenticationToken(
                username, null,grantedAuthorities
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
