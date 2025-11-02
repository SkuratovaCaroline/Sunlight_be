package com.sunlight_cinema.Sunlight_cinema.repository;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import com.sunlight_cinema.Sunlight_cinema.model.User.Role;


@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(String username);
    Optional<User> findByUsername(String username);
    List<User> findByRole(Role role);
}
