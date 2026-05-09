package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.SignInDto;
import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

public class SignInService {
    private final UserRepository userRepository;
    @Autowired
    public SignInService(UserRepository userRepository) {
        this.userRepository  = userRepository;
    }

   /* public UserEntity handlingSignIn(SignInDto request) {
        UserEntity userEntity = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new IllegalArgumentException("Invalid email or password"));


    }*/
}
