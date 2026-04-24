package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.users.User;
import dev.sorokin.eventmanager.users.UserRole;
import org.springframework.stereotype.Service;

import javax.xml.stream.events.EndElement;

@Service
public class EventPermissionService {

    public boolean canModify(
            User currentUser,
            Event event
    ) {
        return event.ownerId().equals(currentUser.id())
                || currentUser.role() == UserRole.ADMIN;
    }
}
