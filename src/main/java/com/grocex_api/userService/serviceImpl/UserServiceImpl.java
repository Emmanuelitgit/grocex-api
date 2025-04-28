package com.grocex_api.userService.serviceImpl;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.DTOMapper;
import com.grocex_api.userService.dto.UserPayloadDTO;
import com.grocex_api.userService.models.RoleSetup;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.dto.UserDTO;
import com.grocex_api.userService.models.UserRole;
import com.grocex_api.userService.repo.RoleSetupRepo;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.userService.service.UserService;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.lang.reflect.Array;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleServiceImpl userRoleServiceImpl;
    private final RoleSetupRepo roleSetupRepo;
    private final RoleSetupServiceImpl roleSetupServiceImpl;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, DTOMapper dtoMapper, PasswordEncoder passwordEncoder, UserRoleServiceImpl userRoleServiceImpl, RoleSetupRepo roleSetupRepo, RoleSetupServiceImpl roleSetupServiceImpl) {
        this.userRepo = userRepo;
        this.dtoMapper = dtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleServiceImpl = userRoleServiceImpl;
        this.roleSetupRepo = roleSetupRepo;
        this.roleSetupServiceImpl = roleSetupServiceImpl;
    }

    /**
     * @description This method is used to save user to the db
     * @param userPayloadDTO
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Transactional
    @Override
    public ResponseEntity<ResponseDTO> createUser(UserPayloadDTO userPayloadDTO) {
       try {
           if (userPayloadDTO  == null){
               ResponseDTO  response = AppUtils.getResponseDto("user payload cannot be null", HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }

           userPayloadDTO.setPassword(passwordEncoder.encode(userPayloadDTO.getPassword()));
           User user = dtoMapper.toUserEntity(userPayloadDTO);
           user.setCreatedAt(ZonedDateTime.now());
           User userResponse = userRepo.save(user);

           Optional<RoleSetup> roleSetupOptional = roleSetupRepo.findById(userPayloadDTO.getRole());
           if (roleSetupOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("role record not found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           RoleSetup role = roleSetupOptional.get();
           userRoleServiceImpl.saveUserRole(userResponse.getId(), userPayloadDTO.getRole());

           UserDTO userDTO = DTOMapper.toUserDTO(userResponse, role.getName());
           ResponseDTO  response = AppUtils.getResponseDto("user record added successfully", HttpStatus.CREATED, userDTO);
           return new ResponseEntity<>(response, HttpStatus.CREATED);
       } catch (Exception e) {
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to get all users from the db
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> getUsers() {
       try{
           List<UserDTO> users = userRepo.getUsersDetails();
           if (users.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           ResponseDTO  response = AppUtils.getResponseDto("users records fetched successfully", HttpStatus.OK, users);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to get user records given the user id.
     * @param userId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> getUserById(UUID userId) {
       try{
           UserDTO user = userRepo.getUsersDetailsByUserId(userId);
           if (user == null){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           ResponseDTO  response = AppUtils.getResponseDto("user records fetched successfully", HttpStatus.OK, user);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to update user records.
     * @param userPayload
     * @param userId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateUser(UserPayloadDTO userPayload, UUID userId) {
        try{
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            User existingData = userOptional.get();
            existingData.setEmail(userPayload.getEmail());
            existingData.setFirstName(userPayload.getFirstName());
            existingData.setLastName(userPayload.getLastName());
            existingData.setUsername(userPayload.getUsername());
            existingData.setPhone(userPayload.getPhone());
            User userResponse = userRepo.save(existingData);

            ResponseEntity<ResponseDTO> role = roleSetupServiceImpl.findRoleById(userPayload.getRole());
            UserDTO userDTOResponse = DTOMapper.toUserDTO(userResponse, role.getBody().getData().toString());

            ResponseDTO  response = AppUtils.getResponseDto("user records updated successfully", HttpStatus.OK, userDTOResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to remove user records from the db.
     * @param userId
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> removeUser(UUID userId) {
        try {
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            userRepo.deleteById(userId);
            ResponseDTO  response = AppUtils.getResponseDto("user record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
