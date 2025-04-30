package org.example.mapper;

import org.example.dto.UserDTO;
import org.example.entity.User;

import java.util.UUID;

public class UserMapper {
    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getPassword(),
                user.getLogin(),
                user.getEmail(),
                user.getPhone()
        );
    }

    public User toEntity(UserDTO dto) {
        return new User(
                dto.getId()/* != null ? dto.getId() : UUID.randomUUID()*/,
                dto.getLogin(),
                dto.getPassword(),
                dto.getEmail(),
                dto.getPhone()
        );
    }
}
