package com.grocex_api.authenticationService;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    private final UserRepo userRepo;

    @Autowired
    public UserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<com.grocex_api.userService.models.User> userOptional = userRepo.findUserByEmail(username);
        if (userOptional.isEmpty()){
           throw new UsernameNotFoundException("Invalid credentials");
        }

        return org.springframework.security.core.userdetails.User.builder()
                .username(username)
                .password(userOptional.get().getPassword())
                .build();
    }
}
