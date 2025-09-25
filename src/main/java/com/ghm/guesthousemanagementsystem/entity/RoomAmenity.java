package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "room_amenity")
public class RoomAmenity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "amenity_id")
    private Amenity amenity;

    @Column(name = "is_enabled")
    private Boolean isEnabled;
}
