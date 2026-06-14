package com.eddieahiati.ecommerce.controllers;

import com.eddieahiati.ecommerce.dtos.UpdateUserRequest;
import com.eddieahiati.ecommerce.dtos.UserDto;
import com.eddieahiati.ecommerce.dtos.UserRequest;
import com.eddieahiati.ecommerce.entities.Profile;
import com.eddieahiati.ecommerce.entities.Role;
import com.eddieahiati.ecommerce.mappers.UserMapper;
import com.eddieahiati.ecommerce.repositories.UserRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserDto userDto) {
        var userEmail = userRepository.findUserByEmail(userDto.getEmail()).orElse(null);
        if(userEmail != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exist"));
        }

        var profile = Profile.builder().loyaltyPoints(1).build();
        var user = userMapper.toUser(userDto);
        user.setProfile(profile);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userDto);
    }

    @GetMapping
    public List<UserRequest> getAllUsers() {
        return userRepository.findAllUsers().stream().map(u -> userMapper.userRequest(u)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserRequest> getUser(@PathVariable(name = "id")Long id) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userMapper.userRequest(user));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable(name = "id")Long id,
            @RequestBody UpdateUserRequest updateUserRequest) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        var userEmail = userRepository.findUserByEmail(updateUserRequest.getEmail()).orElse(null);
        if(userEmail != null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already exist"));
        }

        userMapper.update(updateUserRequest, user);
        userRepository.save(user);

        return ResponseEntity.ok(userMapper.userRequest(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "id")Long id) {
        var user = userRepository.findById(id).orElse(null);
        if(user == null) {
            return ResponseEntity.notFound().build();
        }

        userRepository.delete(user);

        return ResponseEntity.noContent().build();
    }
}
