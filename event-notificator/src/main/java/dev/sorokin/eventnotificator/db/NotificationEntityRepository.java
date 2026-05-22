package dev.sorokin.eventnotificator.db;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationEntityRepository extends JpaRepository<NotificationEntity, Long> {
}
