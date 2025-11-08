package com.sunlight_cinema.Sunlight_cinema;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import com.sunlight_cinema.Sunlight_cinema.model.Role;
import com.sunlight_cinema.Sunlight_cinema.repository.UserRepository;
import com.sunlight_cinema.Sunlight_cinema.repository.RoleRepository;
import com.sunlight_cinema.Sunlight_cinema.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private Role customerRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Мокаем PasswordEncoder
        when(passwordEncoder.encode(any(String.class))).thenAnswer(invocation ->
                "ENCODED_" + invocation.getArgument(0)
        );

        // Создаём роль CUSTOMER
        customerRole = new Role();
        customerRole.setId(3L);
        customerRole.setRoleName("CUSTOMER");

        // Мокаем RoleRepository
        when(roleRepository.findByRoleName("CUSTOMER")).thenReturn(Optional.of(customerRole));
    }

    // ТЕСТ 1: ПАРАМЕТРИЗОВАННЫЙ — создание разных пользователей
    @ParameterizedTest(name = "Создание пользователя: {0}, email: {1}")
    @CsvSource({
            "alice, alice@example.com",
            "bob,   bob@cinema.com",
            "admin, admin@site.ru"
    })
    @DisplayName("Создание пользователя с разными данными")
    void shouldCreateUserWithDifferentData(String username, String email) {
        // Given
        User input = new User();
        input.setUsername(username);
        input.setPasswordHash("123456");
        input.setEmail(email);

        when(userRepository.existsByUsername(username)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L);
            u.setRole(customerRole);
            return u;
        });

        // When
        User result = userService.create(input);

        // Then
        assertNotNull(result.getId());
        assertEquals(username, result.getUsername());
        assertEquals(email, result.getEmail());
        assertEquals("CUSTOMER", result.getRole().getRoleName());
        assertTrue(result.getPasswordHash().startsWith("ENCODED_"));
        verify(userRepository).save(any(User.class));
    }

    // ТЕСТ 2: ОШИБКА — дубликат username
    @Test
    @DisplayName("Ошибка при создании пользователя с существующим username")
    void shouldThrowExceptionWhenUsernameExists() {
        // Given
        User user = new User();
        user.setUsername("existing");
        user.setPasswordHash("pass");

        when(userRepository.existsByUsername("existing")).thenReturn(true);

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            userService.create(user);
        });

        assertEquals("Пользователь уже существует", exception.getMessage());
        verify(userRepository, never()).save(any());
    }
}
