package com.grocex_api.userService.serviceImpl;

import com.grocex_api.exception.AlreadyExistException;
import com.grocex_api.exception.BadRequestException;
import com.grocex_api.exception.NotFoundException;
import com.grocex_api.productService.models.Vendor;
import com.grocex_api.productService.serviceImpl.VendorServiceImpl;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.dto.*;
import com.grocex_api.userService.models.RoleSetup;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.repo.RoleSetupRepo;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.userService.service.UserService;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final DTOMapper dtoMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleServiceImpl userRoleServiceImpl;
    private final RoleSetupRepo roleSetupRepo;
    private final RoleSetupServiceImpl roleSetupServiceImpl;
    private final VendorServiceImpl vendorService;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, DTOMapper dtoMapper, PasswordEncoder passwordEncoder, UserRoleServiceImpl userRoleServiceImpl, RoleSetupRepo roleSetupRepo, RoleSetupServiceImpl roleSetupServiceImpl, VendorServiceImpl vendorService) {
        this.userRepo = userRepo;
        this.dtoMapper = dtoMapper;
        this.passwordEncoder = passwordEncoder;
        this.userRoleServiceImpl = userRoleServiceImpl;
        this.roleSetupRepo = roleSetupRepo;
        this.roleSetupServiceImpl = roleSetupServiceImpl;
        this.vendorService = vendorService;
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
           log.info("In create user method:->>>>>>");
           if (userPayloadDTO  == null){
               ResponseDTO  response = AppUtils.getResponseDto("user payload cannot be null", HttpStatus.BAD_REQUEST);
               return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
           }

           // check if email already exist
           Optional<User> userEmailExist =  userRepo.findUserByEmail(userPayloadDTO.getEmail());
           if (userEmailExist.isPresent()){
               ResponseDTO  response = AppUtils.getResponseDto("email already exist", HttpStatus.ALREADY_REPORTED);
               return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
           }

           // check id username already exist
           Optional<User> usernameExist =  userRepo.findUserByUsername(userPayloadDTO.getUsername());
           if (usernameExist.isPresent()){
               ResponseDTO  response = AppUtils.getResponseDto("username already exist", HttpStatus.ALREADY_REPORTED);
               return new ResponseEntity<>(response, HttpStatus.ALREADY_REPORTED);
           }

           userPayloadDTO.setPassword(passwordEncoder.encode(userPayloadDTO.getPassword()));
           User user = dtoMapper.toUserEntity(userPayloadDTO);
           user.setCreatedAt(ZonedDateTime.now());
           User userResponse = userRepo.save(user);

           // getting the user role name from the role setup db
           Optional<RoleSetup> roleSetupOptional = roleSetupRepo.findById(userPayloadDTO.getRole());
           if (roleSetupOptional.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("role record not found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           RoleSetup role = roleSetupOptional.get();
           userRoleServiceImpl.saveUserRole(userResponse.getId(), userPayloadDTO.getRole());

           Vendor vendor = Vendor.builder()
                   .name(userPayloadDTO.getVendor())
                   .userId(userResponse.getId())
                   .status(userPayloadDTO.getVendorStatus())
                   .createdBy(userResponse.getId())
                   .createdAt(ZonedDateTime.now())
                   .build();
           vendorService.saveVendor(vendor);
           log.info("User created successfully:->>>>>>");
           UserDTO userDTO = DTOMapper.toUserDTO(userResponse, role.getName());
           ResponseDTO  response = AppUtils.getResponseDto("user record added successfully", HttpStatus.CREATED, userDTO);
           return new ResponseEntity<>(response, HttpStatus.CREATED);
       } catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
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
           log.info("In get all users method:->>>>>>");
           List<UserDTOProjection> users = userRepo.getUsersDetails();
           if (users.isEmpty()){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           ResponseDTO  response = AppUtils.getResponseDto("users records fetched successfully", HttpStatus.OK, users);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
           ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
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
           log.info("In get user by id method:->>>>>>");
           UserDTOProjection user = userRepo.getUsersDetailsByUserId(userId);
           if (user == null){
               ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
           }
           ResponseDTO  response = AppUtils.getResponseDto("user records fetched successfully", HttpStatus.OK, user);
           return new ResponseEntity<>(response, HttpStatus.OK);
       } catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to update user records.
     * @param userPayload
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> updateUser(UserPayloadDTO userPayload) {
        try{
            log.info("In update user method:->>>>>>{}", userPayload);
            User existingData = userRepo.findById(userPayload.getId())
                    .orElseThrow(()-> new NotFoundException("user record not found"));

            existingData.setEmail(userPayload.getEmail() !=null ? userPayload.getEmail() : existingData.getEmail());
            existingData.setFirstName(userPayload.getFirstName() !=null ? userPayload.getFirstName() : existingData.getFirstName());
            existingData.setLastName(userPayload.getLastName() !=null ? userPayload.getLastName() : existingData.getLastName());
            existingData.setUsername(userPayload.getUsername() !=null ? userPayload.getUsername() : existingData.getUsername());
            existingData.setPhone(userPayload.getPhone() !=null ? userPayload.getPhone() : existingData.getPhone());
            User userResponse = userRepo.save(existingData);

            // getting the role name from the role setup db
           RoleSetup role =  new RoleSetup();
            if (userPayload.getRole() != null){
                RoleSetup roleData  = roleSetupRepo.findById(userPayload.getRole())
                        .orElseThrow(()-> new NotFoundException("role record not found"));
                role.setName(roleData.getName());
            }

            // updating vendor details
            Vendor vendor = Vendor.builder()
                    .name(userPayload.getVendor())
                    .status(userPayload.getVendorStatus())
                    .build();
            vendorService.updateVendor(vendor);

            log.info("user updated successfully:->>>>>>");
            UserDTO userDTOResponse = DTOMapper.toUserDTO(userResponse, role.getName());
            ResponseDTO  response = AppUtils.getResponseDto("user records updated successfully", HttpStatus.OK, userDTOResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
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
            log.info("In remove user method:->>>>>>");
            Optional<User> userOptional = userRepo.findById(userId);
            if (userOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no user record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            userRepo.deleteById(userId);
            log.info("user removed successfully:->>>>>>");
            ResponseDTO  response = AppUtils.getResponseDto("user record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to fetch users with their products.ie products that belong to them.
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 1st may 2025
     */
    public ResponseEntity<ResponseDTO> getUsersAndProducts(){
      try{
          log.info("In getUsersAndProducts method:->>>>>>>");
          List<UserProductProjection> userProductRes = userRepo.getUsersAndProducts();
          if (userProductRes.isEmpty()){
              ResponseDTO  response = AppUtils.getResponseDto("no record found", HttpStatus.NOT_FOUND);
              return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
          }

          // storing the data(user and products) here as list
          List<Object> res = new ArrayList<>();
          // to hold products with user email as key
          Map<String, List<Object>> products = new HashMap<>();
          // looping to set user details and products of the user
          for (UserProductProjection userProduct: userProductRes){
              Map<String, Object> data = new HashMap<>();
              data.put("id", userProduct.getUserId());
              data.put("email", userProduct.getEmail());
              data.put("full name", userProduct.getFullName());
              data.put("vendor", userProduct.getVendor());

              if (!products.containsKey(userProduct.getEmail())){
                  products.put(userProduct.getEmail(), new ArrayList<>());
              }
              Map<String, Object> productItems = new HashMap<>();
              productItems.put("id", userProduct.getProductId());
              productItems.put("name", userProduct.getProduct());
              productItems.put("unit price", userProduct.getUnitPrice());
              productItems.put("quantity", userProduct.getQuantity());
              productItems.put("category", userProduct.getCategory());

              products.get(userProduct.getEmail()).add(productItems);

              data.put("products", products.get(userProduct.getEmail()));
              res.add(data);
          }

          // removing duplicates
          Set<Object> setRes = new HashSet<>(res);
          log.info("user products fetched success:->>>>>>");
          ResponseDTO  response = AppUtils.getResponseDto("user products fetched successfully", HttpStatus.OK, setRes);
          return new ResponseEntity<>(response, HttpStatus.OK);
      } catch (Exception e) {
          log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
          ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
          return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
      }
    }
}
