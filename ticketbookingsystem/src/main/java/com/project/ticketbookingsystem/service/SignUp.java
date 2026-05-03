package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUp {
    private final UserRepository userRepository;
    @Autowired
    public SignUp (UserRepository userRepository) {
        this.userRepository  = userRepository;
    }

}
