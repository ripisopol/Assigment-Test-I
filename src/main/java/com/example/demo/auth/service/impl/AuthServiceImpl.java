package com.example.demo.auth.service.impl;

import com.example.demo.auth.dto.LoginRequestDto;
import com.example.demo.auth.dto.LoginResponseDto;
import com.example.demo.auth.service.AuthService;
import com.example.demo.users.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;

  // Use a ConcurrentHashMap to store invalidated tokens
  private final Map<String, Instant> tokenBlacklist = new ConcurrentHashMap<>();

  @Override
  public String generateToken(Authentication authentication) {
    Instant now = Instant.now();
    String scope = authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.joining(" "));

    JwtClaimsSet claims = JwtClaimsSet.builder()
            .issuer("self")
            .issuedAt(now)
            .expiresAt(now.plus(1, ChronoUnit.HOURS))
            .subject(authentication.getName())
            .claim("scope", scope)
            .build();

    return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
  }

  @Override
  public LoginResponseDto login(LoginRequestDto loginRequest) {
    log.info("Attempting login for user: {}", loginRequest.getUsername());
    try {
      Authentication authentication = authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
      );

      String token = generateToken(authentication);

      log.info("User logged in successfully: {}", loginRequest.getUsername());
      LoginResponseDto response = new LoginResponseDto();
      response.setToken(token);
      response.setMessage("Login successful");
      return response;
    } catch (Exception e) {
      log.error("Login failed for user: {}. Reason: {}", loginRequest.getUsername(), e.getMessage());
      throw e;
    }
  }

  @Override
  public void logout(String token) {
    log.info("Attempting to logout user");
    try {
      Jwt jwt = jwtDecoder.decode(token);
      Instant expiration = jwt.getExpiresAt();
      if (expiration != null) {
        tokenBlacklist.put(token, expiration);
        log.info("Token blacklisted for user: {}", jwt.getSubject());
      }
    } catch (JwtException e) {
      log.warn("Invalid token provided for logout", e);
    }
  }

  @Override
  public String extractTokenFromRequest(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }

  @Override
  public boolean isTokenValid(String token) {
    log.debug("Validating token");
    if (tokenBlacklist.containsKey(token)) {
      log.info("Token found in blacklist");
      return false;
    }

    try {
      Jwt jwt = jwtDecoder.decode(token);
      boolean isValid = !Instant.now().isAfter(Objects.requireNonNull(jwt.getExpiresAt()));
      log.info("Token validation result: {}", isValid);
      return isValid;
    } catch (JwtException e) {
      log.warn("Token validation failed", e);
      return false;
    }
  }

  // Periodically clean up expired tokens from the blacklist
  // This method could be scheduled to run at regular intervals
  public void cleanupTokenBlacklist() {
    Instant now = Instant.now();
    tokenBlacklist.entrySet().removeIf(entry -> now.isAfter(entry.getValue()));
  }
}