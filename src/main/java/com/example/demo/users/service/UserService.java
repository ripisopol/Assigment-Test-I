package com.example.demo.users.service;

import com.example.demo.users.dto.UserDetailsDto;
import com.example.demo.users.dto.RegisterRequestDto;

public interface UserService {
    void registerUser(RegisterRequestDto registerRequest);
    UserDetailsDto getUserDetails(String username);
    void updateUserDetails(String username, UserDetailsDto userDetailsDto);
    void deleteUser(String username);

}
