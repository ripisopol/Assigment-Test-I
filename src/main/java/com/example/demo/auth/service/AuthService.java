package com.example.demo.auth.service;

import com.example.demo.auth.dto.LoginRequestDto;
import com.example.demo.auth.dto.LoginResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;

public interface AuthService {
  String generateToken(Authentication authentication);
  LoginResponseDto login(LoginRequestDto loginRequest);
  void logout(String token);
  String extractTokenFromRequest(HttpServletRequest request);
  boolean isTokenValid(String token);
}
