package com.project.ticketbookingsystem.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateProfileDto {

    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(
            regexp = "^$|.{6,}",
            message = "Password must be at least 6 characters")
    @Size(min = 0)
    private String password;

    @NotNull(message = "National ID is required")
    @Min(value = 10000000000000L, message = "National ID must be exactly 14 digits")
    @Max(value = 99999999999999L, message = "National ID must be exactly 14 digits")
    private Long nationalId;

    @NotBlank(message = "Phone number is required")

    @Size(min = 11, max = 11,
            message = "Phone number must be exactly 11 digits")

    @Pattern(regexp = "\\d+",
            message = "Phone number must contain only digits")
    private String phoneNumber;

    private LocalDateTime createdAt;
}
