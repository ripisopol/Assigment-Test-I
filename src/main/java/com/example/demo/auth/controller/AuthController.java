package com.example.demo.auth.controller;

import com.example.demo.auth.dto.LoginRequestDto;
import com.example.demo.auth.dto.LoginResponseDto;
import com.example.demo.auth.entity.UserAuth;
import com.example.demo.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@Validated
@Log
public class AuthController {
  private final AuthService authService;
  private final AuthenticationManager authenticationManager;


  @PostMapping("/login")
  public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto loginRequest, HttpServletResponse response) {
    LoginResponseDto loginResponse = authService.login(loginRequest);

    Cookie cookie = new Cookie("sid", loginResponse.getToken());
    cookie.setHttpOnly(true);
    cookie.setSecure(true); // set to true if using HTTPS
    cookie.setPath("/");
    response.addCookie(cookie);

    return ResponseEntity.ok(loginResponse);
  }
  @PostMapping("/logout")
  public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response) {
    String token = authService.extractTokenFromRequest(request);
    authService.logout(token);

    // Clear the cookie
    Cookie cookie = new Cookie("sid", null);
    cookie.setPath("/");
    cookie.setHttpOnly(true);
    cookie.setMaxAge(0);
    response.addCookie(cookie);

    return ResponseEntity.ok("Logged out successfully");
  }
}