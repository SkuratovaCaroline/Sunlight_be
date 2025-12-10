package com.sunlight_cinema.user.service;

import com.sunlight_cinema.user.dto.UserDto;
import com.sunlight_cinema.user.dto.UserProfileDto;
import com.sunlight_cinema.user.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

    List<UserDto> getAll();

    UserDto getById(Long id);

    UserDto create(UserDto userDto);

    UserDto update(UserDto userDto, MultipartFile avatar);

    // ИСПРАВЛЕНО: Возвращает UserDto вместо User
    UserDto create(User user);

    // ИСПРАВЛЕНО: Возвращает UserDto вместо User. Использование findUserById.
    UserDto update(Long id, User updatedUser);

    void delete(Long id);

    List<UserDto> findByName(String name);

    UserProfileDto findProfileById(Long id);
}