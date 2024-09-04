package com.example.demo.users.controller;

import com.example.demo.users.dto.UserDetailsDto;
import com.example.demo.users.dto.RegisterRequestDto;
import com.example.demo.users.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@Validated
@Log
public class UserController {
  private final UserService userService;

  @PostMapping("/register")
  public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterRequestDto registerRequest) {
    userService.registerUser(registerRequest);
    return ResponseEntity.ok("User registered successfully");
  }

  @GetMapping("/details")
  public ResponseEntity<UserDetailsDto> getUserDetails(Authentication authentication) {
    UserDetailsDto userDetails = userService.getUserDetails(authentication.getName());
    return ResponseEntity.ok(userDetails);
  }

  @PutMapping("/details")
  public ResponseEntity<String> updateUserDetails(@Valid @RequestBody UserDetailsDto userDetailsDto, Authentication authentication) {
    userService.updateUserDetails(authentication.getName(), userDetailsDto);
    return ResponseEntity.ok("User details updated successfully");
  }

  @DeleteMapping("/details")
  public ResponseEntity<String> deleteUser(Authentication authentication) {
    userService.deleteUser(authentication.getName());
    return ResponseEntity.ok("User deleted successfully");
  }
}
