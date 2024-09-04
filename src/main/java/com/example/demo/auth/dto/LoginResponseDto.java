package com.example.demo.auth.dto;

import lombok.Data;

@Data
public class LoginResponseDto {
  private String message;
  private String token;
}
