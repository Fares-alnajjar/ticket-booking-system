package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.SignUpDto;

import com.project.ticketbookingsystem.model.UserEntity;
import com.project.ticketbookingsystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SignUpService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public SignUpService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository  = userRepository;
        this.passwordEncoder = passwordEncoder;

    }

    public UserEntity register(SignUpDto request){
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
                .password(passwordEncoder.encode(request.getPassword())) // must be encoded
                .phoneNumber(request.getPhoneNumber())
                .role("user")
                .build();
        return userRepository.save(usere) ;

    }
    public void updateUser(UserEntity User) {
        userRepository.save(User);
    }


    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // used to tell how many Users in admin page
    public long getTotalUsers() {
        return userRepository.count();
    }

    // to put users in dashboard
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll();
    }

    public UserEntity getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
    }

}
