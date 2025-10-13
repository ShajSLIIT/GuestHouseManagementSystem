package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Room;
import com.ghm.guesthousemanagementsystem.entity.RoomAmenity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoomAmenityRepo extends JpaRepository<RoomAmenity, UUID> {
    List<RoomAmenity> findByRoomId(UUID roomId);

    UUID room(Room room);
}
