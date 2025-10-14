package com.sunlight_cinema.Sunlight_cinema.repository;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
