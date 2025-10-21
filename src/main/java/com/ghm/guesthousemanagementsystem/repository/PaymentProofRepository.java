// PaymentProofRepository.java
package com.naveen.guesthousemanagementsystem.repository;


import com.naveen.guesthousemanagementsystem.entity.PaymentProof;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PaymentProofRepository extends JpaRepository<PaymentProof, UUID> {

    // Find by booking ID
    Optional<PaymentProof> findByBookingBookingId(UUID bookingId);

    // Find by reference ID
    @Query("SELECT pp FROM PaymentProof pp WHERE pp.booking.referenceId = :referenceId")
    Optional<PaymentProof> findByBookingReferenceId(@Param("referenceId") String referenceId);

    // Check existence by reference ID
    @Query("SELECT COUNT(pp) > 0 FROM PaymentProof pp WHERE pp.booking.referenceId = :referenceId")
    boolean existsByBookingReferenceId(@Param("referenceId") String referenceId);

    // Find by reference ID with booking eager fetch
    @Query("SELECT pp FROM PaymentProof pp JOIN FETCH pp.booking WHERE pp.booking.referenceId = :referenceId")
    Optional<PaymentProof> findByBookingReferenceIdWithBooking(@Param("referenceId") String referenceId);
}