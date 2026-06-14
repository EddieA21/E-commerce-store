package com.eddieahiati.ecommerce.repositories;

import com.eddieahiati.ecommerce.entities.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @EntityGraph(attributePaths = "profile")
    @Query("SELECT u FROM User u")
    List<User> findAllUsers();

    @EntityGraph(attributePaths = "profile")
    @Query("SELECT u FROM User u WHERE u.email = :email")
    Optional<User> findUserByEmail(@Param("email")String email);
}
