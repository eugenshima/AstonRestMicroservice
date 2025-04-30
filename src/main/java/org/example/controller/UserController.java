package org.example.controller;

import org.example.dto.UserDTO;
import org.example.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserController {
    List<UserDTO> FindAllUsers();
    UserDTO FindUserByID(UUID ID);
    UUID CreateUser(UserDTO userDTO);
    boolean DeleteUserByID(UUID ID);
    UUID UpdateUser(UserDTO userDTO);
}
