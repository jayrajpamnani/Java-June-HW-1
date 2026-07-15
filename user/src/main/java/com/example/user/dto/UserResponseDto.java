package com.example.user.dto;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String fullName;
    private LocalDate registeredDate;
    private Boolean active;
}