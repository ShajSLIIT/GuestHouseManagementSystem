package com.naveen.guesthousemanagementsystem.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "booking_addon")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingAddon {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", columnDefinition = "CHAR(36)")
    private UUID id;

    @Column(name = "booking_id", nullable = false)
    private Long bookingId;

    @Column(name = "addon_id", nullable = false, columnDefinition = "CHAR(36)")
    private UUID addonId;

    @Column(nullable = false)
    private Integer quantity = 1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addon_id", insertable = false, updatable = false)
    private Addon addon;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}