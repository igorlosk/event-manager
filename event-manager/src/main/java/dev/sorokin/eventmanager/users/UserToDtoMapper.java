package dev.sorokin.eventmanager.users;

import org.springframework.stereotype.Component;

@Component
public class UserToDtoMapper {

    public UserDto userToDto(User user) {
        return new UserDto(
                user.id(),
                user.login()
        );
    }
}
