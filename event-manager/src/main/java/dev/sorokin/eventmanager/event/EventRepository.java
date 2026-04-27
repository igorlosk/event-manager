package dev.sorokin.eventmanager.event;

import dev.sorokin.eventmanager.registration.RegistrationEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<EventEntity, Long> {


    @EntityGraph(attributePaths = "registrations")
    Optional<EventEntity> findById(Long id);

    @Transactional
    @Modifying
    @Query("""
            UPDATE EventEntity event SET 
               event.name = :name,
               event.maxPlaces = :maxPlaces,
               event.date = :date,
               event.cost = :cost,
               event.duration = :duration,
               event.locationId = :locationId
               WHERE event.id = :id
            
            """)
    void updateLocation(
            @Param("id") Integer id,
            @Param("name") String name,
            @Param("maxPlaces") Integer maxPlaces,
            @Param("date") LocalDateTime date,
            @Param("cost") Integer cost,
            @Param("duration") Integer duration,
            @Param("locationId") Integer locationId
    );

    @EntityGraph(attributePaths = "registrations")
    List<EventEntity> findAllByOwnerId(Integer ownerId);

    @Query("""
            SELECT e FROM EventEntity e
            WHERE (:name IS NULL OR e.name = :name)
            AND (:placesMin IS NULL OR e.maxPlaces >= :placesMin)
            AND (:placesMax IS NULL OR e.maxPlaces <= :placesMax)
            AND (:dateStartAfter IS NULL OR e.date >= :dateStartAfter)
            """)
    List<EventEntity> search(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter
    );
}
