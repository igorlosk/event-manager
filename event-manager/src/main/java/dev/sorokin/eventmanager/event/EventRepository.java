package dev.sorokin.eventmanager.event;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
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
            AND (cast(:dateStartAfter AS TIMESTAMP) IS NULL OR e.date >= :dateStartAfter)
            AND (cast(:dateStartBefore AS TIMESTAMP) IS NULL OR e.date <= :dateStartBefore)
            AND (:costMin IS NULL OR e.cost >= :costMin)
            AND (:costMax IS NULL OR e.cost <= :costMax)
            AND (:durationMin IS NULL OR e.duration >= :durationMin)
            AND (:durationMax IS NULL OR e.duration <= :durationMax)
            AND (:locationId IS NULL OR e.locationId = :locationId)
            AND (:eventStatus IS NULL OR e.status = :eventStatus)            
            """)
    List<EventEntity> search(
            @Param("name") String name,
            @Param("placesMin") Integer placesMin,
            @Param("placesMax") Integer placesMax,
            @Param("dateStartAfter") LocalDateTime dateStartAfter,
            @Param("dateStartBefore") LocalDateTime dateStartBefore,
            @Param("costMin") Integer costMin,
            @Param("costMax") Integer costMax,
            @Param("durationMin") Integer durationMin,
            @Param("durationMax") Integer durationMax,
            @Param("locationId") Long locationId,
            @Param("eventStatus") EventStatus eventStatus
    );

//    @Query("SELECT a FROM EventEntity a WHERE a.ownerId = :userId")
//    @EntityGraph(value = "events_by_user")
//    List<EventEntity> findAllEvents(@Param("user")Long userId);
}
