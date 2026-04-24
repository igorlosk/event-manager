package dev.sorokin.eventmanager.events;

import dev.sorokin.eventmanager.users.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    @Query("SELECT a FROM EventEntity a WHERE a. = :userId")
    @EntityGraph(value = "events_by_user")
    List<EventEntity> findAllEvents(@Param("user")Long userId);

}
