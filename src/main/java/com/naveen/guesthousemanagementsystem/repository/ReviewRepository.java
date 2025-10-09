package com.naveen.guesthousemanagementsystem.repository;

import com.naveen.guesthousemanagementsystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    /** Find all reviews for a specific booking */
    List<Review> findByBookingId(Long bookingId);

    /** Check if review exists for a booking */
    boolean existsByBookingId(Long bookingId);

    /** Find review by ID and booking ID */
    Optional<Review> findByIdAndBookingId(UUID id, Long bookingId);
}