package com.sunlight_cinema.Sunlight_cinema;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import com.sunlight_cinema.Sunlight_cinema.model.Role;
import com.sunlight_cinema.Sunlight_cinema.repository.UserRepository;
import com.sunlight_cinema.Sunlight_cinema.repository.RoleRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
public class UserDbConnectionTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @AfterEach
    public void cleanup() {
        userRepository.deleteAll();

        String resetSequenceSql =
                "ALTER SEQUENCE users_user_id_seq RESTART WITH 1";

        entityManager.createNativeQuery(resetSequenceSql).executeUpdate();
    }

    @Test
    @DisplayName("Проверка сохранения и чтения роли CUSTOMER из PostgreSQL")
    void shouldSaveAndRetrieveUserWithCustomerRole() {
        // 1. Получаем роль CUSTOMER из таблицы roles
        Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Role CUSTOMER not found in DB"));

        // 2. Создаём пользователя
        String testUsername = "db_test_user";
        User user = new User();
        user.setUsername(testUsername);
        user.setEmail("db@test.com");
        user.setPasswordHash("hash123");
        user.setRole(customerRole);

        // 3. Сохраняем
        User savedUser = userRepository.save(user);

        // 4. Проверяем
        assertNotNull(savedUser.getId(), "ID пользователя должен быть сгенерирован");
        assertEquals("CUSTOMER", savedUser.getRole().getRoleName(), "Роль должна быть CUSTOMER");

        // 5. Читаем обратно
        Optional<User> foundUserOptional = userRepository.findByUsername(testUsername);
        assertTrue(foundUserOptional.isPresent(), "Пользователь должен быть найден в БД");
        User foundUser = foundUserOptional.get();

        assertEquals("CUSTOMER", foundUser.getRole().getRoleName(),
                "Роль должна корректно читаться из БД");
    }

    @Test
    @DisplayName("Проверка сохранения и чтения роли ADMIN из PostgreSQL")
    void shouldSaveAndRetrieveUserWithAdminRole() {
        // 1. Получаем роль ADMIN
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Role ADMIN not found in DB"));

        // 2. Создаём пользователя
        String testUsername = "db_admin_user";
        User user = new User();
        user.setUsername(testUsername);
        user.setEmail("admin@test.com");
        user.setPasswordHash("admin_hash");
        user.setRole(adminRole);

        // 3. Сохраняем
        userRepository.save(user);

        // 4. Читаем обратно
        Optional<User> foundUserOptional = userRepository.findByUsername(testUsername);
        assertTrue(foundUserOptional.isPresent());
        User foundUser = foundUserOptional.get();

        assertEquals("ADMIN", foundUser.getRole().getRoleName(),
                "Роль должна корректно читаться из БД");
    }
}
