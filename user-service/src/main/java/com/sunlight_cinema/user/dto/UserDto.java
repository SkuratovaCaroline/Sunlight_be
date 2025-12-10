package com.sunlight_cinema.user.dto;

import com.sunlight_cinema.user.model.User;
import java.util.List;

public record UserDto(
        Long id,
        String name,           // firstName
        String surname       // lastName
        //List<MovieDto> favourites   // пока можно null или пустой список
) {
    public static UserDto from(User user) {
        return new UserDto(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
                //List.of() // потом заполним
        );
    }
}