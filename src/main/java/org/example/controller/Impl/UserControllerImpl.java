package org.example.controller.Impl;

import org.example.controller.UserController;
import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.service.Impl.UserServiceImpl;
import org.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

public class UserControllerImpl implements UserController {
    private static final Logger log = LoggerFactory.getLogger(UserControllerImpl.class);
    private final UserService userService;
    private static final UserController INSTANCE = new UserControllerImpl();


    private UserControllerImpl() {
        this.userService = UserServiceImpl.getInstance();
    }
    public static UserController getInstance() {
        return INSTANCE;
    }

    @Override
    public List<UserDTO> FindAllUsers() {
        log.info("Finding all users");
        List<UserDTO> users = userService.findAllUsers();
        log.info("Found {} users", users.size());
        return users;
    }

    @Override
    public UserDTO FindUserByID(UUID ID) {
        log.info("FindUserByID");
        return userService.FindUserByID(ID);
    }

    @Override
    public UUID CreateUser(UserDTO userDTO) {
        log.info("CreateUser");
        return userService.CreateUser(userDTO);
    }

    @Override
    public boolean DeleteUserByID(UUID ID) {
        log.info("DeleteUserByID");
        return userService.DeleteUserByID(ID);
    }

    @Override
    public UUID UpdateUser(UserDTO userDTO) {
        log.info("UpdateUser");
        return userService.UpdateUser(userDTO);
    }
}
