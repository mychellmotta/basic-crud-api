package com.mychellmotta.basiccrudapi.repository;

import com.mychellmotta.basiccrudapi.model.Thing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ThingRepository extends JpaRepository<Thing, UUID> {
    @Query("SELECT t FROM Thing t WHERE t.description LIKE %?1%")
    List<Thing> hasDescription(String description);

    Optional<Thing> findByDescription(String description);
}
