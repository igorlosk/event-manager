package dev.sorokin.eventmanager.location;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface LocationRepository extends JpaRepository<LocationEntity, Integer> {


    @Transactional
    @Modifying
    @Query("""
            UPDATE LocationEntity loc SET 
            loc.name = :name,
            loc.address = :address,
            loc.capacity = :capacity,
            loc.description = :description
            """)
    void updateLocation(
            @Param("name") String name,
            @Param("address") String address,
            @Param("capacity") Integer capacity,
            @Param("description") String description
    );
}
