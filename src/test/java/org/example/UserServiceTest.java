package org.example;

import org.example.dto.UserDTO;
import org.example.entity.User;
import org.example.mapper.UserMapper;
import org.example.repository.UserRepository;
import org.example.service.Impl.UserServiceImpl;
import org.example.utils.CustomExceptions.DatabaseException;
import org.example.utils.CustomExceptions.UserNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.sql.SQLException;
import java.util.*;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    // Тест 1: Успешное получение всех пользователей
    @Test
    void findAllUsers_shouldReturnListOfUserDTOs_whenUsersExist() throws SQLException {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();

        User user1 = new User(id1, "login1", "pass1", "email1@test.com", "123");
        User user2 = new User(id2, "login2", "pass2", "email2@test.com", "456");
        List<User> mockUsers = Arrays.asList(user1, user2);

        UserDTO userDTO1 = new UserDTO(id1, "login1", "pass1", "email1@test.com", "123");
        UserDTO userDTO2 = new UserDTO(id2, "login2", "pass2", "email2@test.com", "456");

        when(userRepository.findAllUsers()).thenReturn(mockUsers);
        when(userMapper.toDto(user1)).thenReturn(userDTO1);
        when(userMapper.toDto(user2)).thenReturn(userDTO2);

        List<UserDTO> result = userService.findAllUsers();

        assertEquals(2, result.size());  // Исправлено с 3 на 2
        assertEquals(userDTO1, result.get(0));
        assertEquals(userDTO2, result.get(1));

        verify(userRepository).findAllUsers();
        verify(userMapper).toDto(user1);
        verify(userMapper).toDto(user2);
    }

    // Тест 2: Получение всех пользователей с ошибкой БД
    @Test
    void findAllUsers_shouldThrowDatabaseException_whenSQLExceptionOccurs() throws SQLException {
        when(userRepository.findAllUsers()).thenThrow(new SQLException("DB error"));

        assertThrows(DatabaseException.class, () -> userService.findAllUsers());
    }

    // Тест 3: Успешное получение пользователя по ID
    @Test
    void findUserById_shouldReturnUserDTO_whenUserExists() throws SQLException {
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "login", "pass", "email@test.com", "123");
        UserDTO userDTO = new UserDTO(userId, "login", "pass","email@test.com", "123");

        when(userRepository.FindUserByID(userId)).thenReturn(Optional.of(user));
        when(userMapper.toDto(user)).thenReturn(userDTO);

        UserDTO result = userService.FindUserByID(userId);

        assertEquals(userDTO, result);
        verify(userRepository).FindUserByID(userId);
    }

    // Тест 4: Попытка получить несуществующего пользователя
    @Test
    void findUserById_shouldThrowUserNotFoundException_whenUserNotExists() throws SQLException {
        UUID userId = UUID.randomUUID();
        when(userRepository.FindUserByID(userId)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.FindUserByID(userId));
    }

    // Тест 5: Создание нового пользователя
    @Test
    void createUser_shouldReturnUUID_whenUserIsValid() throws SQLException {
        UserDTO userDTO = new UserDTO(null, "newLogin", "pass", "new@email.com", "123");
        User user = new User(null, "newLogin", "encryptedPass", "new@email.com", "123");
        UUID expectedId = UUID.randomUUID();

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.CreateUser(user)).thenReturn(expectedId);

        UUID result = userService.CreateUser(userDTO);

        assertEquals(expectedId, result);
        verify(userRepository).CreateUser(user);
    }

    // Тест 6: Ошибка при создании пользователя
    @Test
    void createUser_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        UserDTO userDTO = new UserDTO(null, "newLogin", "pass","new@email.com", "123");
        User user = new User(null, "newLogin", "encryptedPass", "new@email.com", "123");

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.CreateUser(user)).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.CreateUser(userDTO));
        assertTrue(exception.getMessage().contains("Failed to create user"));
    }

    // Тест 7: Успешное удаление пользователя
    @Test
    void deleteUserById_shouldReturnTrue_whenUserExists() throws SQLException {
        UUID userId = UUID.randomUUID();
        when(userRepository.DeleteUserByID(userId)).thenReturn(true);

        boolean result = userService.DeleteUserByID(userId);

        assertTrue(result);
        verify(userRepository).DeleteUserByID(userId);
    }

    // Тест 8: Ошибка при удалении пользователя
    @Test
    void deleteUserById_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        UUID userId = UUID.randomUUID();
        when(userRepository.DeleteUserByID(userId)).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.DeleteUserByID(userId));
        assertTrue(exception.getMessage().contains("Failed to create user")); // Обратите внимание на сообщение
    }

    // Тест 9: Успешное обновление пользователя
    @Test
    void updateUser_shouldReturnUUID_whenUpdateIsSuccessful() throws SQLException {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(userId, "updatedLogin", "encryptedPass","updated@email.com", "456");
        User user = new User(userId, "updatedLogin", "encryptedPass", "updated@email.com", "456");

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.UpdateUser(user)).thenReturn(userId);

        UUID result = userService.UpdateUser(userDTO);

        assertEquals(userId, result);
        verify(userRepository).UpdateUser(user);
    }

    // Тест 10: Ошибка при обновлении пользователя
    @Test
    void updateUser_shouldThrowRuntimeException_whenSQLExceptionOccurs() throws SQLException {
        UUID userId = UUID.randomUUID();
        UserDTO userDTO = new UserDTO(userId, "updatedLogin", "encryptedPass","updated@email.com", "456");
        User user = new User(userId, "updatedLogin", "encryptedPass", "updated@email.com", "456");

        when(userMapper.toEntity(userDTO)).thenReturn(user);
        when(userRepository.UpdateUser(user)).thenThrow(new SQLException("DB error"));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> userService.UpdateUser(userDTO));
        assertTrue(exception.getMessage().contains("Failed to create user"));
    }
}
