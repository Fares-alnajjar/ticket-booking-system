package com.project.ticketbookingsystem.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private Long id;
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^$|.{6,}",
            message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "National ID is required")
    @Pattern(
            regexp = "\\d{14}",
            message = "National ID must be exactly 14 digits"
    )
    private String nationalId;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "\\d{11}", message = "Phone number must be exactly 11 digits")
    private String phoneNumber;
}
