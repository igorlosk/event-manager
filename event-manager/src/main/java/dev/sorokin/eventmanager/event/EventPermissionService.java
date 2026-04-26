package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.users.User;
import dev.sorokin.eventmanager.users.UserRole;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class EventPermissionService {

    public boolean canModify(
            User currentUser,
            Event event
    ) {
        Long userId = currentUser.id();
        Long eventId = Long.valueOf(event.id());

        return Objects.equals(userId, eventId) || currentUser.role() == UserRole.ADMIN;
    }
}
