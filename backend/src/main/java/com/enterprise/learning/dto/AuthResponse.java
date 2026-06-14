package com.enterprise.learning.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthResponse {
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private String token;
    private String refreshToken;
    private Set<String> roles;
    private String message;
}
