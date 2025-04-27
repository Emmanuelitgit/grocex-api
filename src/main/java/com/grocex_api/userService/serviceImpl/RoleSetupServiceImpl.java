package com.grocex_api.userService.serviceImpl;

import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.models.RoleSetup;
import com.grocex_api.userService.repo.RoleSetupRepo;
import com.grocex_api.userService.service.RoleSetupService;
import com.grocex_api.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class RoleSetupServiceImpl implements RoleSetupService {

    private final RoleSetupRepo roleSetupRepo;

    @Autowired
    public RoleSetupServiceImpl(RoleSetupRepo roleSetupRepo) {
        this.roleSetupRepo = roleSetupRepo;
    }

    @Override
    public ResponseEntity<ResponseDTO> saveRole(RoleSetup roleSetup) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> updateRole(RoleSetup roleSetup, UUID roleId) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> findRoleById(UUID roleId) {
        try {
            Optional<RoleSetup> roleSetupOptional = roleSetupRepo.findById(roleId);
            if (roleSetupOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("role record not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            RoleSetup roleSetup = roleSetupOptional.get();
            ResponseDTO  response = AppUtils.getResponseDto("role records fetched successfully", HttpStatus.OK, roleSetup);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }catch (Exception e) {
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
