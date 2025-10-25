package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Property;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {
    // Even though rooms is LAZY, this method overrides that just for this query

    @EntityGraph(attributePaths = {"rooms"})
    Optional<Property> findWithRoomsById(UUID id);
}
