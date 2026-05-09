package com.project.ticketbookingsystem.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInDto {
    @NotBlank(message = "Please select an authorization type")
    private String role;
    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "password is required")
    private String password;
}
