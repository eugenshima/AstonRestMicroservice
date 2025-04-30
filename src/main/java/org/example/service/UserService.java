package org.example.service;

import org.example.dto.UserDTO;
import org.example.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserService {
    List<UserDTO> findAllUsers();
    UserDTO FindUserByID(UUID id);
    UUID CreateUser(UserDTO userDto);
    boolean DeleteUserByID(UUID ID);
    UUID UpdateUser(UserDTO userDto);
}
