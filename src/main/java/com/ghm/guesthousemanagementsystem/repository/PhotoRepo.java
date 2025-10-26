package com.ghm.guesthousemanagementsystem.repository;

import com.ghm.guesthousemanagementsystem.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PhotoRepo  extends JpaRepository<Photo, UUID> {

    List<Photo> findByRoomId(UUID roomId);
    List<Photo> findByPropertyId(UUID propertyId);
}
