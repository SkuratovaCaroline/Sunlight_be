package com.sunlight_cinema.Sunlight_cinema.service;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import com.sunlight_cinema.Sunlight_cinema.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.sunlight_cinema.Sunlight_cinema.model.User.Role;
import java.util.List;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

@Service
//@Slf4j
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository repo;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repo, PasswordEncoder passwordEncoder) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll() {
        log.trace("TRACE: Запрос всех пользователей");
        log.debug("DEBUG: Вызов repo.findAll()");
        List<User> users = repo.findAll();
        log.info("INFO: Успешно получено {} пользователей", users.size());
        return users;
    }

    @Override
    public User getById(Long id) {
        log.trace("TRACE: Поиск пользователя по ID: {}", id);
        log.debug("DEBUG: Вызов repo.findById({})", id);
        User user = repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("WARN: Пользователь с ID {} не найден", id);
                    return new RuntimeException("User not found");
                });
        log.info("INFO: Пользователь найден: ID={}, username={}", user.getId(), user.getUsername());
        return user;
    }

    @Override
    public User create(User user) {
        log.trace("TRACE: Начало создания пользователя: {}",
                user.getUsername() != null ? user.getUsername() : "null");

        // Проверка username
        if (user.getUsername() == null || user.getUsername().isBlank()) {
            log.warn("WARN: Попытка создать пользователя с пустым или null username");
            throw new IllegalArgumentException("Username не может быть пустым");
        }

        // Проверка на дубликат
        if (repo.existsByUsername(user.getUsername())) {
            log.warn("WARN: Пользователь с username '{}' уже существует", user.getUsername());
            throw new RuntimeException("Пользователь уже существует");
        }

        // Проверка пароля
        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            log.warn("WARN: Попытка создать пользователя с пустым паролем");
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        // Установка роли: customer по умолчанию
        if (user.getRole() == null) {
            user.setRole(Role.CUSTOMER);
            // ИСПРАВЛЕНО: Роль CUSTOMER установлена — меняем лог-сообщение с GUEST
            log.info("INFO: Роль не указана — установлена CUSTOMER для пользователя: {}", user.getUsername());
        } else {
            log.debug("DEBUG: Указана роль: {} для пользователя: {}", user.getRole(), user.getUsername());
        }

        // Шифрование пароля
        log.debug("DEBUG: Шифрование пароля для пользователя: {}", user.getUsername());
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        // Сохранение
        log.info("INFO: Сохранение нового пользователя: {}", user.getUsername());
        User saved = repo.save(user);

        log.info("INFO: Пользователь успешно создан. ID: {}, Роль: {}", saved.getId(), saved.getRole());

        // Демонстрация ERROR
        // log.error("ERROR: Тестовая ошибка", new RuntimeException("Test"));

        return saved;
    }

    @Override
    public User update(Long id, User updatedUser) {
        log.trace("TRACE: Запрос на обновление пользователя ID: {}", id);

        // 1. Проверяем существование пользователя
        User existing = getById(id);  // уже логируется внутри getById()


        // 3. Проверка username на уникальность (если меняется)
        if (updatedUser.getUsername() != null
                && !updatedUser.getUsername().isBlank()
                && !updatedUser.getUsername().equals(existing.getUsername())) {

            if (repo.existsByUsername(updatedUser.getUsername())) {
                log.warn("WARN: Username '{}' уже занят при обновлении пользователя ID: {}",
                        updatedUser.getUsername(), id);
                throw new RuntimeException("Username уже используется");
            }
        }

        // 4. Проверка пароля — если передан, шифруем
        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isBlank()) {
            log.debug("DEBUG: Обновление пароля для пользователя ID: {}", id);
            existing.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
        } else {
            log.debug("DEBUG: Пароль не передан — оставляем прежний");
        }

        // 5. Обновление полей (только если не null)
        if (updatedUser.getUsername() != null && !updatedUser.getUsername().isBlank()) {
            log.debug("DEBUG: Обновление username: {} → {}", existing.getUsername(), updatedUser.getUsername());
            existing.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getEmail() != null) {
            log.debug("DEBUG: Обновление email: {}", updatedUser.getEmail());
            existing.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getFirstName() != null) {
            existing.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            existing.setLastName(updatedUser.getLastName());
        }

        // 6. Обновление роли — только админ может менять на ADMIN/MODERATOR
        if (updatedUser.getRole() != null && updatedUser.getRole() != existing.getRole()) {
            log.info("INFO: Попытка смены роли с {} на {} для пользователя ID: {}",
                    existing.getRole(), updatedUser.getRole(), id);

            // Разрешаем только если текущий пользователь — ADMIN
            // (в реальном проекте — через @PreAuthorize или SecurityContext)
            if (!hasCurrentUserRole("ADMIN")) {
                log.error("ERROR: Только ADMIN может менять роли. Текущая роль: {}", getCurrentUserRole());
                throw new SecurityException("Недостаточно прав для смены роли");
            }

            existing.setRole(updatedUser.getRole());
            log.info("INFO: Роль успешно изменена на {}", updatedUser.getRole());
        }

        // 7. Сохранение
        log.info("INFO: Сохранение обновлённого пользователя ID: {}", id);
        User saved = repo.save(existing);
        log.info("INFO: Пользователь успешно обновлён. ID: {}, Роль: {}", saved.getId(), saved.getRole());

        return saved;
    }

    @Override
    public void delete(Long id) {
        log.trace("TRACE: Удаление пользователя ID: {}", id);
        if (!repo.existsById(id)) {
            log.warn("WARN: Попытка удалить несуществующего пользователя ID: {}", id);
            throw new RuntimeException("User not found");
        }
        log.info("INFO: Удаление пользователя ID: {}", id);
        repo.deleteById(id);
        log.info("INFO: Пользователь успешно удалён. ID: {}", id);
    }

    // Дополнительный метод для демонстрации ERROR
    public void triggerError() {
        log.info("INFO: Запуск метода с ошибкой для логирования ERROR");
        try {
            throw new RuntimeException("Тестовая ошибка в UserServiceImpl");
        } catch (Exception e) {
            log.error("ERROR: Критическая ошибка в UserServiceImpl", e);
        }
    }

    private boolean hasCurrentUserRole(String requiredRole) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + requiredRole));
    }

    private String getCurrentUserRole() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return "ANONYMOUS";
        }
        return auth.getAuthorities().stream()
                .map(a -> a.getAuthority().replace("ROLE_", ""))
                .findFirst()
                .orElse("UNKNOWN");
    }
}