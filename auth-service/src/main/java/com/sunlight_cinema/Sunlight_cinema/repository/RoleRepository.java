package com.sunlight_cinema.Sunlight_cinema.repository;

import com.sunlight_cinema.Sunlight_cinema.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(String name);
}
