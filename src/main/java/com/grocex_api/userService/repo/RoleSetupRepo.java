package com.grocex_api.userService.repo;

import com.grocex_api.userService.models.RoleSetup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RoleSetupRepo extends JpaRepository<RoleSetup, UUID> {
}
