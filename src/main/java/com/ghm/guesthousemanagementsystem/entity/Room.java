package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "room")
public class Room {

    @Id
    @Column(name = "room_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

//    @Column(name = "property_id", columnDefinition = "BINARY(16)")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "property_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_room_property")
    )
    private Property property;

    @Column(name = "room_type", length = 100)
    private String roomType;

    @Column(name = "room_number", length = 50)
    private String roomNumber;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "price_per_night", precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "max_occupancy")
    private Integer maxOccupancy;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    // Relationship to room_amenity
//    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<RoomAmenity> roomAmenities = new ArrayList<>();
}
