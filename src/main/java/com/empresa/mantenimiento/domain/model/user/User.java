package com.empresa.mantenimiento.domain.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder(toBuilder = true)
public class User {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;
    private Role role;
    private Boolean active;
    private LocalDateTime lockedUntil;
    private Integer failedAttempts;
}
