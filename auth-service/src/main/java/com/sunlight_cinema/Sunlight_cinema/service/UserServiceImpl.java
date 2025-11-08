package com.sunlight_cinema.Sunlight_cinema.service;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import com.sunlight_cinema.Sunlight_cinema.model.Role;
import com.sunlight_cinema.Sunlight_cinema.repository.UserRepository;
import com.sunlight_cinema.Sunlight_cinema.repository.RoleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.Authentication;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private final UserRepository repo;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository repo,
                           PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository) {
        this.repo = repo;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
    }

    @Override
    public List<User> getAll() {
        log.trace("TRACE: Запрос всех пользователей");
        List<User> users = repo.findAll();
        log.info("INFO: Успешно получено {} пользователей", users.size());
        return users;
    }

    @Override
    public User getById(Long id) {
        log.trace("TRACE: Поиск пользователя по ID: {}", id);
        return repo.findById(id)
                .orElseThrow(() -> {
                    log.warn("WARN: Пользователь с ID {} не найден", id);
                    return new RuntimeException("User not found");
                });
    }

    @Override
    public User create(User user) {
        log.trace("TRACE: Начало создания пользователя: {}", user.getUsername());

        if (user.getUsername() == null || user.getUsername().isBlank()) {
            throw new IllegalArgumentException("Username не может быть пустым");
        }

        if (repo.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Пользователь уже существует");
        }

        if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new IllegalArgumentException("Пароль не может быть пустым");
        }

        // Установка роли CUSTOMER по умолчанию
        if (user.getRole() == null) {
            Role customerRole = roleRepository.findByRoleName("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("Роль CUSTOMER не найдена в БД"));
            user.setRole(customerRole);
            log.info("INFO: Роль не указана — установлена CUSTOMER для пользователя: {}", user.getUsername());
        } else {
            log.debug("DEBUG: Указана роль: {} для пользователя: {}", user.getRole().getRoleName(), user.getUsername());
        }

        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));

        User saved = repo.save(user);
        log.info("INFO: Пользователь успешно создан. ID: {}, Роль: {}", saved.getId(), saved.getRole().getRoleName());
        return saved;
    }

    @Override
    public User update(Long id, User updatedUser) {
        User existing = getById(id);

        if (updatedUser.getUsername() != null
                && !updatedUser.getUsername().isBlank()
                && !updatedUser.getUsername().equals(existing.getUsername())) {
            if (repo.existsByUsername(updatedUser.getUsername())) {
                throw new RuntimeException("Username уже используется");
            }
            existing.setUsername(updatedUser.getUsername());
        }

        if (updatedUser.getPasswordHash() != null && !updatedUser.getPasswordHash().isBlank()) {
            existing.setPasswordHash(passwordEncoder.encode(updatedUser.getPasswordHash()));
        }

        if (updatedUser.getEmail() != null) {
            existing.setEmail(updatedUser.getEmail());
        }

        if (updatedUser.getFirstName() != null) {
            existing.setFirstName(updatedUser.getFirstName());
        }

        if (updatedUser.getLastName() != null) {
            existing.setLastName(updatedUser.getLastName());
        }

        // Обновление роли
        if (updatedUser.getRole() != null && !updatedUser.getRole().equals(existing.getRole())) {
            log.info("INFO: Попытка смены роли с {} на {} для пользователя ID: {}",
                    existing.getRole().getRoleName(), updatedUser.getRole().getRoleName(), id);

            if (!hasCurrentUserRole("ADMIN")) {
                throw new SecurityException("Недостаточно прав для смены роли");
            }

            existing.setRole(updatedUser.getRole());
            log.info("INFO: Роль успешно изменена на {}", updatedUser.getRole().getRoleName());
        }

        User saved = repo.save(existing);
        log.info("INFO: Пользователь успешно обновлён. ID: {}, Роль: {}", saved.getId(), saved.getRole().getRoleName());
        return saved;
    }

    @Override
    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repo.deleteById(id);
        log.info("INFO: Пользователь успешно удалён. ID: {}", id);
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
