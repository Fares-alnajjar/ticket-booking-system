package com.project.ticketbookingsystem.service;

import com.project.ticketbookingsystem.dto.SignUpDto;

import com.project.ticketbookingsystem.dto.UpdateProfileDto;

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
        //  Name
        if (request.getName() == null || request.getName().isBlank())
            throw new IllegalArgumentException("Name is required");

        //  Email
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new IllegalArgumentException("Email is required");
        if (!request.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$"))
            throw new IllegalArgumentException("Invalid email format");

        // Password
        if (request.getPassword() == null || request.getPassword().isBlank())
            throw new IllegalArgumentException("Password is required");
        if (request.getPassword().length() < 6)
            throw new IllegalArgumentException("Password must be at least 6 characters");

        //  National ID
        if (request.getNationalId() == null)
            throw new IllegalArgumentException("National ID is required");
        if (request.getNationalId().toString().length() != 14)
            throw new IllegalArgumentException("National ID must be exactly 14 digits");

        //  Phone Number
        if (request.getPhoneNumber() == null || request.getPhoneNumber().isBlank())
            throw new IllegalArgumentException("Phone number is required");
        if (!request.getPhoneNumber().matches("\\d{11}"))
            throw new IllegalArgumentException("Phone number must be exactly 11 digits");

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



    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void updateUser(UpdateProfileDto request) {

        UserEntity user = userRepository.findById(request.getId()).get();


        // 1. Conditional Duplicate Checks (Only if the value is provided and different)
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }
            user.setEmail(request.getEmail());
        }

        if (request.getNationalId() != null && !request.getNationalId().equals(user.getNationalId())) {
            if (userRepository.existsByNationalId(request.getNationalId())) {
                throw new IllegalArgumentException("National ID already in use");
            }
            user.setNationalId(request.getNationalId());
        }

        if (request.getPhoneNumber() != null && !request.getPhoneNumber().equals(user.getPhoneNumber())) {
            if (userRepository.existsByPhoneNumber(request.getPhoneNumber())) {
                throw new IllegalArgumentException("Phone number already in use");
            }
            user.setPhoneNumber(request.getPhoneNumber());
        }

        // 2. Update other fields only if they are not null
        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }


        userRepository.save(user);
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
