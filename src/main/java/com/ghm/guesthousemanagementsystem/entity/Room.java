package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "property_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_room_property")
    )
    private Property property;

    @Column(name = "price_per_night", precision = 10, scale = 2)
    private BigDecimal pricePerNight;

    @Column(name = "max_occupancy")
    private Integer maxOccupancy;

}
