package com.grocex_api.userService.dto;

import com.grocex_api.userService.models.User;
import org.springframework.stereotype.Component;

@Component
public class DTOMapper {

    /* this method takes user object and transform it to userDTO*/
    public static UserDTO toUserDTO(User user, String role){
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getUsername(),
                role
        );
    }

    public User toUserEntity(UserPayloadDTO user){
        return new User(
                user.getCreatedAt(),
                user.getCreatedBy(),
                user.getEmail(),
                user.getFirstName(),
                user.getId(),
                user.getLastName(),
                user.getPassword(),
                user.getPhone(),
                user.getUsername()
        );
    }

}
