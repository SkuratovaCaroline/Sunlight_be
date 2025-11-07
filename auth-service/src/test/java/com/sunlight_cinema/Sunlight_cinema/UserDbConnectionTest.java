package com.sunlight_cinema.Sunlight_cinema;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import com.sunlight_cinema.Sunlight_cinema.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test") // Использует настройки профиля test, если он существует
public class UserDbConnectionTest {

    @Autowired
    private UserRepository userRepository;

    // В интеграционных тестах сервисный слой не нужен, но нужно замокать
    // PasswordEncoder, чтобы не получить ошибку "No bean of type PasswordEncoder found"
    // если он используется в UserServiceImpl.
    @MockBean
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void cleanup() {
        // Очищаем тестовые данные после каждого теста
        userRepository.deleteAll();
    }

    @Test
    @DisplayName("Проверка сохранения и чтения роли CUSTOMER из PostgreSQL")
    void shouldSaveAndRetrieveUserWithCustomerRole() {
        // 1. Arrange (Подготовка)
        String testUsername = "db_test_user";
        User user = new User();
        user.setUsername(testUsername);
        user.setEmail("db@test.com");
        user.setPasswordHash("hash123");
        // Устанавливаем ENUM-значение в Java (CUSTOMER)
        user.setRole(User.Role.CUSTOMER);

        // 2. Act (Действие) - Сохранение в реальную БД
        User savedUser = userRepository.save(user);

        // 3. Assert (Проверка) - Проверка сохраненного объекта
        assertNotNull(savedUser.getId(), "ID пользователя должен быть сгенерирован");
        assertEquals(User.Role.CUSTOMER, savedUser.getRole(), "Роль должна быть CUSTOMER в Java-объекте");

        // 4. Act - Считывание из БД (проверка конвертера)
        Optional<User> foundUserOptional = userRepository.findByUsername(testUsername);

        // 5. Assert - Проверка считанного объекта
        assertTrue(foundUserOptional.isPresent(), "Пользователь должен быть найден в БД");
        User foundUser = foundUserOptional.get();

        // Если этот assertEquals пройдет, это означает, что:
        // 1. Hibernate успешно записал 'customer' (нижний регистр) в ENUM столбец.
        // 2. RoleConverter успешно прочитал 'customer' и вернул User.Role.CUSTOMER (верхний регистр).
        assertEquals(User.Role.CUSTOMER, foundUser.getRole(),
                "Конвертер должен корректно вернуть Java ENUM CUSTOMER из БД");
    }

    @Test
    @DisplayName("Проверка сохранения и чтения роли ADMIN из PostgreSQL")
    void shouldSaveAndRetrieveUserWithAdminRole() {
        // 1. Arrange (Подготовка)
        String testUsername = "db_admin_user";
        User user = new User();
        user.setUsername(testUsername);
        user.setEmail("admin@test.com");
        user.setPasswordHash("admin_hash");
        // Устанавливаем роль ADMIN
        user.setRole(User.Role.ADMIN);

        // 2. Act (Действие) - Сохранение в реальную БД
        userRepository.save(user);

        // 3. Act - Считывание из БД
        Optional<User> foundUserOptional = userRepository.findByUsername(testUsername);

        // 4. Assert - Проверка считанного объекта
        assertTrue(foundUserOptional.isPresent());
        User foundUser = foundUserOptional.get();

        // Проверяем, что конвертер правильно обработал роль ADMIN
        assertEquals(User.Role.ADMIN, foundUser.getRole(),
                "Конвертер должен корректно вернуть Java ENUM ADMIN из БД");
    }
}