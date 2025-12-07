package com.grocex_api.userService.repo;

import com.grocex_api.userService.models.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRoleRepo extends JpaRepository<UserRole, UUID> {
    Optional<UserRole> findByUserIdAndRoleId(UUID userId, UUID roleId);
}
