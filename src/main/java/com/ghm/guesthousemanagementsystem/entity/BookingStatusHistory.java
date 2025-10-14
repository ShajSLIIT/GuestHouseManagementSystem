package com.ghm.guesthousemanagementsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ghm.guesthousemanagementsystem.enums.BookingStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY) // LAZY avoids heavy joins unless needed
    @JoinColumn(name = "booking_id", nullable = false) // FK column
    private Booking booking;

    @Enumerated(EnumType.STRING)
    @Column(name="from_status", length=50)
    private BookingStatus fromStatus;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name="to_status", length=50)
    private BookingStatus toStatus;

    @Column(name = "changed_at", insertable = false, updatable = false)
    private LocalDateTime changedAt;

    @Column(name="reason")
    private String reason;
}
