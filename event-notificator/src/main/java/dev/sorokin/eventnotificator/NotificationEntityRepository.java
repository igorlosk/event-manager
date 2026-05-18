package dev.sorokin.eventnotificator;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {
}
