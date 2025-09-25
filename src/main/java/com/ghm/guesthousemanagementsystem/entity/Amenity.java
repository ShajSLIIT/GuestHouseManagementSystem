package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "amenity")
public class Amenity {

    @Id
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(length = 100)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "icon_url", length = 255)
    private String iconUrl;

    @Column(name = "is_active")
    private Boolean isActive;
}
