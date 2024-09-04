package com.example.demo.users.service.impl;

import com.example.demo.users.dto.UserDetailsDto;
import com.example.demo.users.dto.RegisterRequestDto;
import com.example.demo.users.entity.Users;
import com.example.demo.users.repository.UserRepository;
import com.example.demo.users.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void registerUser(RegisterRequestDto registerRequest) {
    if (userRepository.findByUsername(registerRequest.getUsername()).isPresent()) {
      throw new RuntimeException("Username already exists");
    }

    Users user = registerRequest.toEntity();
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    userRepository.save(user);
    log.info("User registered: " + user.getUsername());
  }

  @Override
  public UserDetailsDto getUserDetails(String username) {
    Users user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));
    return new UserDetailsDto(user);
  }

  @Override
  public void updateUserDetails(String username, UserDetailsDto userDetailsDto) {
    Users user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    user.setName(userDetailsDto.getName());
    user.setMobile(userDetailsDto.getMobile());
    user.setAddress(userDetailsDto.getAddress());

    userRepository.save(user);
    log.info("User details updated: " + username);
  }

  @Override
  public void deleteUser(String username) {
    Users user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found"));

    userRepository.delete(user);
    log.info("User deleted: " + username);
  }
}
