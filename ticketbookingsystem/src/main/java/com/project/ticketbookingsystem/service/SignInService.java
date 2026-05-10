package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.SignInDto;
import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
@Service
public class SignInService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    @Autowired
    public SignInService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserEntity handlingSignIn(SignInDto request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));
        if (!passwordEncoder.matches(request.getPassword(), userEntity.getPassword())){
            throw new IllegalArgumentException("Invalid password");
        }
          return userEntity;
    }
}
