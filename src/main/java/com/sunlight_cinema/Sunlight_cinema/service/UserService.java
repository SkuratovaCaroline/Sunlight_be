package com.sunlight_cinema.Sunlight_cinema.service;

import com.sunlight_cinema.Sunlight_cinema.model.User;
import java.util.List;

public interface UserService {
    List<User> getAll();
    User getById(Long id);
    User create(User user);
    User update(Long id, User user);
    void delete(Long id);
}
