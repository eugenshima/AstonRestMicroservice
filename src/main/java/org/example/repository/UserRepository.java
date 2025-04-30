package org.example.repository;

import org.example.entity.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
    List<User> findAllUsers() throws SQLException;
    Optional<User> FindUserByID(UUID id) throws SQLException;
    UUID CreateUser(User user) throws SQLException;
    boolean DeleteUserByID(UUID ID) throws SQLException;
    UUID UpdateUser(User user) throws SQLException;
}
