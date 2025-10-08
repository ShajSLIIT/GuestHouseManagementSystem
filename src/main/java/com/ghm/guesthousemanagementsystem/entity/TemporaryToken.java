package com.ghm.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
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
@Table(name="temporary_tokens")
public class TemporaryToken {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name="booking_id", length=50)
    private String bookingId;

    @Column(length=50)
    private String token;

    @Column(name = "expires_at")
    private LocalDate expiresAt;
}
