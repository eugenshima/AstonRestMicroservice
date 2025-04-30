package org.example.service.Impl;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.Impl.UserRepositoryImpl;
import org.example.repository.UserRepository;
import org.example.service.UserService;
import org.example.utils.CustomExceptions.DatabaseException;
import org.example.utils.CustomExceptions.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserServiceImpl implements UserService{
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private static final UserService INSTANCE = new UserServiceImpl();

    private UserServiceImpl() {
        this.userRepository = UserRepositoryImpl.getInstance();
        this.userMapper = new UserMapper();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<UserDTO> findAllUsers() {
        log.info("Looking for users");
        try {
            List<User> users = userRepository.findAllUsers();
            log.info("Users found: {}", users);
            return users.stream()
                    .map(userMapper::toDto)
                    .toList();
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch user from database", e);
        }

    }

    @Override
    public UserDTO FindUserByID(UUID id) {
        log.info("Looking for user by ID: {}", id);

        try {
            Optional<User> user = userRepository.FindUserByID(id);

            if (user.isPresent()) {
                log.info("User found: {}", user.get());
                return userMapper.toDto(user.get());
            } else {
                throw new UserNotFoundException("User not found with ID: " + id);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Failed to fetch user from database", e);
        }
    }

    @Override
    public UUID CreateUser(UserDTO userDTO) {
        try {
            User user = userMapper.toEntity(userDTO);
            return userRepository.CreateUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean DeleteUserByID(UUID ID) {
        try {
            return userRepository.DeleteUserByID(ID);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }

    @Override
    public UUID UpdateUser(UserDTO userDto) {
        try {
           User user = userMapper.toEntity(userDto);
           return userRepository.UpdateUser(user);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create user: " + e.getMessage(), e);
        }
    }
}
