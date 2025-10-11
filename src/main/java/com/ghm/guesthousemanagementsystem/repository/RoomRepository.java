package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Room;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomRepository extends JpaRepository<Room, UUID> {
    List<Room> findByPropertyId(UUID propertyId);

    @Query("SELECT r FROM Room r WHERE r.id = :id")
    @EntityGraph(attributePaths = {"property", "roomAmenities", "roomAmenities.amenity"})
    Optional<Room> findDetailsById(@Param("id")UUID id);
}
