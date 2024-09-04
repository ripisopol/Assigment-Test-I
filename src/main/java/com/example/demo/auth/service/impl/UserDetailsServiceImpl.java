package com.example.demo.auth.service.impl;

import com.example.demo.auth.entity.UserAuth;
import com.example.demo.users.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  private final UserRepository userRepository;

  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    var userData = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    return new UserAuth(userData);
  }
}