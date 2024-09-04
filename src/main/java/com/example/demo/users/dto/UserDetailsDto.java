package com.example.demo.users.dto;

import com.example.demo.users.entity.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserDetailsDto {
    private String username;
    private String email;
    @NotBlank(message = "Name is required")
    private String name;

    @Pattern(regexp = "^\\+?[1-9]\\d{1,14}$", message = "Invalid mobile number")
    private String mobile;

    @NotBlank(message = "Address is required")
    private String address;

    public UserDetailsDto(Users user) {
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
        this.mobile = user.getMobile();
        this.address = user.getAddress();
    }
}