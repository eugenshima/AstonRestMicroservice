package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private UUID id;          // PRIMARY KEY
    private String login;     // NOT NULL
    private String password;  // NOT NULL
    private String phone;     // UNIQUE, nullable
    private String email;     // UNIQUE, nullable
}
