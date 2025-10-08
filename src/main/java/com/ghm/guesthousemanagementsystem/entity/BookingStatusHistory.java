package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="booking_status_history")
public class BookingStatusHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name="booking_id")
    private int bookingId;

    @Column(name="from_status", length=50)
    private String fromStatus;

    @Column(name="to_status", length=50)
    private String toStatus;

    @Column(name = "changed_at")
    private LocalDateTime changedAt;
}
