package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SignUpService {
    private final UserRepository userRepository;
    @Autowired
    public SignUpService(UserRepository userRepository) {
        this.userRepository  = userRepository;
    }

}
