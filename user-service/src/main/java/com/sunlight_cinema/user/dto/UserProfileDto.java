package com.sunlight_cinema.user.dto;

import com.sunlight_cinema.user.model.User;

import java.util.List;

public record UserProfileDto(
        Long id,
        String name,
        String surname,
        String login,
        String avatar
        //List<MovieDto> favourites,
        //List<ChatDto> chats
) {
    public static UserProfileDto from(User user) {
        return new UserProfileDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getUsername(),
                user.getAvatarUrl()
                //List.of(),  // favourites — пока пусто
                //List.of()   // chats — пока пусто
        );
    }
}