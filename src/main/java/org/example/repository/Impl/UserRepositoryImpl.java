package org.example.repository.Impl;

import org.example.entity.User;
import org.example.repository.UserRepository;
import org.example.utils.DBConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UserRepositoryImpl extends DBConnectionFactory implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(UserRepositoryImpl.class);
    private static final UserRepository INSTANCE = new UserRepositoryImpl();

    private UserRepositoryImpl() {}

    public static UserRepository getInstance() {
        return INSTANCE;
    }

    @Override
    public List<User> findAllUsers() throws SQLException{
        List<User> users = new ArrayList<>();
        String SelectString = "Select id, login, password, email, phone from notetag.users";
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SelectString);
             ResultSet resultSet = preparedStatement.executeQuery();) {

            while (resultSet.next()) {
                User user = new User();
                String uuidStr = resultSet.getString("id");
                UUID uuid = UUID.fromString(uuidStr);
                user.setId(uuid);
                user.setLogin(resultSet.getString("login"));
                user.setPassword(resultSet.getString("password"));
                user.setEmail(resultSet.getString("email"));
                user.setPhone(resultSet.getString("phone"));
                users.add(user);
            }
        }
        return users;
    }

    @Override
    public Optional<User> FindUserByID(UUID id) throws SQLException{
        String sql = "SELECT id, login, password, email, phone FROM notetag.users WHERE id=?";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setObject(1, id);  // Подставляем UUID в SQL-запрос

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    User user = new User();
                    user.setId(resultSet.getObject("id", UUID.class));
                    user.setPassword(resultSet.getString("password"));
                    user.setLogin(resultSet.getString("login"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPhone(resultSet.getString("phone"));
                    log.info("user: " + user);
                    return Optional.of(user);
                }
            }
        }
        return Optional.empty();  // Если пользователь не найден
    }

    @Override
    public UUID CreateUser(User user) throws SQLException {
        String sql = "INSERT INTO notetag.users (id, login, password, phone, email) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            UUID userId = UUID.randomUUID();
            user.setId(userId);

            preparedStatement.setObject(1, userId);
            preparedStatement.setString(2, user.getLogin());
            preparedStatement.setObject(3, user.getPassword());
            preparedStatement.setObject(4, user.getPhone());
            preparedStatement.setString(5, user.getEmail());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to create user");
            }
            return userId;
        }
    }

    @Override
    public boolean DeleteUserByID(UUID ID) throws SQLException{
        String DeleteUser = "DELETE FROM notetag.users WHERE id=?";

        try(Connection connection = DBConnectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(DeleteUser)) {

            preparedStatement.setObject(1, ID);

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Failed to delete user");
            }
            return true;
        }
    }

    @Override
    public UUID UpdateUser(User user) throws SQLException{
        String updateUserSQL = "UPDATE notetag.users SET login=?, password=?, phone=?, email=? WHERE id=?";
        try (Connection connection = DBConnectionFactory.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(updateUserSQL)) {
            preparedStatement.setString(1, user.getLogin());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getPhone());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setObject(5, user.getId());
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("User not found or not updated");
            }
        }
        return user.getId();
    }
}

