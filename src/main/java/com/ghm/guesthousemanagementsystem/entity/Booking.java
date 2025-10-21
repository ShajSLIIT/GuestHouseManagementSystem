package com.naveen.guesthousemanagementsystem.entity;

//import com.ghm.guesthousemanagementsystem.enums.BookingStatus;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "booking_id", columnDefinition = "BINARY(16)")
    private UUID bookingId;

//    @ManyToOne(fetch =  FetchType.EAGER)
//    @JoinColumn(name = "property_id", updatable = false)
//    private Property property;

    @Column(name = "reference_id", length = 100)
    private String referenceId;

    @Column(name = "guest_name", length = 200)
    private String guestName;

    @Column(name = "guest_email", length = 100)
    private String guestEmail;

    @Column(name = "guest_phone", length = 20)
    private String guestPhone;

    @Column(name = "check_in_date")
    private LocalDate checkInDate;

    @Column(name = "check_out_date")
    private LocalDate checkOutDate;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

//    @Enumerated(EnumType.STRING)
//    @Column(name="status", length = 20)
//    private BookingStatus status;

    @Column(name = "no_of_rooms")
    private int noOfRooms;

    @Column(name = "no_of_guests")
    private int noOfGuests;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

//    @Column(name="customer_unique_id", length = 100)
//    private String customerUniqueId;                      //Only needs if customer can create accounts

    @Column(name = "is_paid")
    private boolean isPaid;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "expired_at")
    private LocalDateTime expiredAt;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(length = 100)
    private String token;

//    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BookingRoom> bookingRooms = new ArrayList<>();
//
//    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<TemporaryToken> temporaryTokens = new ArrayList<>();
//
//    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<BookingStatusHistory> statusHistory = new ArrayList<>();

    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingAddon> bookingAddons = new ArrayList<>();

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private Review bookingReview;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true)
    private PaymentProof paymentProof;

    // Helper method to get addons
//    public List<Addon> getAddons() {
//        if (bookingAddons == null) return new ArrayList<>();
//        return bookingAddons.stream()
//                .map(BookingAddon::getAddon)
//                .collect(Collectors.toList());
//    }
}
