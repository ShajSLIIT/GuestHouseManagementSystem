package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;


@Table(name = "photo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Photo {

    @Id
    @Column(name = "photo_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    private Property property;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column
    private String caption;

    @Column(name = "is_main")
    private Boolean isMain;

}

