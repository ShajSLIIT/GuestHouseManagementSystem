package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="booking_room")
public class BookingRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "booking_id", updatable = false)
    private Booking booking;

    @ManyToOne
    @JoinColumn(name = "room_id", updatable = false)
    private Room room;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    public BookingRoom(Booking booking,
                       Room room,
                       @NotNull LocalDate checkInDate,
                       @NotNull LocalDate checkOutDate) {
        this.booking = booking;
        this.room = room;
        this.startDate = checkInDate;
        this.endDate = checkOutDate;
    }
}
