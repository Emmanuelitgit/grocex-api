package com.grocex_api.userService.repo;

import com.grocex_api.userService.dto.UserDTO;
import com.grocex_api.userService.dto.UserDTOProjection;
import com.grocex_api.userService.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepo extends JpaRepository<User, UUID> {

    @Query(value = "SELECT BIN_TO_UUID(u.id) AS id, u.first_name,u.last_name, u.email, u.phone, u.username, rs.name AS role FROM user_tb u " +
            "JOIN user_role_tb ur ON u.id = ur.user_id " +
            "JOIN role_setup_tb rs ON ur.role_id=rs.id ", nativeQuery = true)
    List<UserDTOProjection> getUsersDetails();


    @Query(value = "SELECT BIN_TO_UUID(u.id) AS id, u.first_name, u.last_name, u.email, u.phone, u.username, rs.name AS role " +
            "FROM user_tb u " +
            "JOIN user_role_tb ur ON u.id = ur.user_id " +
            "JOIN role_setup_tb rs ON ur.role_id = rs.id " +
            "WHERE u.id =? ", nativeQuery = true)
    UserDTOProjection getUsersDetailsByUserId(UUID userId);


    @Query(value = "SELECT BIN_TO_UUID(u.id) AS id, u.first_name, u.last_name, u.email, u.phone, u.username, rs.name AS role " +
            "FROM user_tb u " +
            "JOIN user_role_tb ur ON u.id = ur.user_id " +
            "JOIN role_setup_tb rs ON ur.role_id = rs.id " +
            "WHERE u.email =? ", nativeQuery = true)
    UserDTOProjection getUsersDetailsByUserEmail(String email);


    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);
}
