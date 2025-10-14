//package com.ghm.guesthousemanagementsystem.entity;
//
//import jakarta.persistence.*;
//
//import java.util.UUID;
//
//@Entity
//public class Property {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    @Column(columnDefinition = "BINARY(16)")
//    private UUID property_id;
//
//    private String property_name;
//
//    private String property_value;
//}


package com.ghm.guesthousemanagementsystem.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.*;

@Entity
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
    @Column(name = "check_in_time")
    private java.time.LocalTime checkInTime;

    @Column(nullable = false)
    private String email;

    @Column(name = "check_out_time")
    private java.time.LocalTime checkOutTime;

    @Column(name = "coverImageUrl")
    private String coverImageUrl;

    @JsonIgnore
    @OneToMany(
            mappedBy = "property",
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Room> rooms = new ArrayList<>();

    public void addRooms(Room room){
        this.rooms.add(room);
    }
    public void removeRoom(Room room){
        this.rooms.remove(room);
    }

}

