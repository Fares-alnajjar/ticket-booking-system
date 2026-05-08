package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.SignUpDto;
import com.project.ticketbookingsystem.model.UserEntity;
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

    public UserEntity rsgister(SignUpDto request){
        if(userRepository.existsByEmail(request.getEmail())){
            throw new IllegalArgumentException("Email already in use");
        }
        if(userRepository.existsByNationalId(request.getNationalId())){
            throw new IllegalArgumentException("National Id already in use");
        }
        if(userRepository.existsByPhoneNumber(request.getPhoneNumber())){
            throw new IllegalArgumentException("Phone Number already in use");
        }

        UserEntity usere = UserEntity.builder()
                .name(request.getName())
                .email(request.getEmail())
                .nationalId(request.getNationalId())
                .password(request.getPassword()) // must be encoded
                .phoneNumber(request.getPhoneNumber())
                .build();
        return userRepository.save(usere) ;
    }
}
