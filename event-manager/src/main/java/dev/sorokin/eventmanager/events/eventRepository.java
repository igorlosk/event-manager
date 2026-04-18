package dev.sorokin.eventmanager.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface eventRepository extends JpaRepository<EventEntity, Long> {
}
