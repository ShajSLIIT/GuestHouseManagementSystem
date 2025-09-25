package com.ghm.guesthousemanagementsystem.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "property")
public class Property {

    @Id
    @Column(name = "property_id", columnDefinition = "BINARY(16)")
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;
    private String location;
    private String city;
    private String country;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "total_rooms")
    private Integer totalRooms;
    private String email;
    @Column(name = "check_in_time")
    private java.time.LocalTime checkInTime;

    @Column(name = "check_out_time")
    private java.time.LocalTime checkOutTime;

    @Column(name = "coverImageUrl")
    private String coverImageUrl;

//     ManyToOne relationship with AdminUser
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "manager_id", foreignKey = @ForeignKey(name = "fk_property_manager"))  // This FK column in property table
//    private AdminUser manager;

    @OneToMany(
            mappedBy = "property",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<Room> rooms = new ArrayList<>();

    // // Uncomment when pushing to main
//    @OneToMany(mappedBy = "property")
//    private List<Photo> photos;

    public void addRooms(Room room){
        this.rooms.add(room);
    }
    public void removeRoom(Room room){
        this.rooms.remove(room);
    }

}

