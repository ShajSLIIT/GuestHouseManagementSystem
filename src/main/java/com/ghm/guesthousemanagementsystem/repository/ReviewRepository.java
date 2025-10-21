package com.naveen.guesthousemanagementsystem.repository;


import com.naveen.guesthousemanagementsystem.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    @Query("SELECT r FROM Review r WHERE r.booking.id = :bookingId")
    List<Review> findByBookingId(@Param("bookingId") UUID bookingId);

    @Query("SELECT COUNT(r) > 0 FROM Review r WHERE r.booking.id = :bookingId")
    boolean existsByBookingId(@Param("bookingId") UUID bookingId);

    @Query("SELECT r FROM Review r WHERE r.booking.id = :bookingId")
    Optional<Review> findFirstByBookingId(@Param("bookingId") UUID bookingId);

    @Query("SELECT r FROM Review r WHERE r.booking.property.id = :propertyId")
    List<Review> findByBookingPropertyId(@Param("propertyId") UUID propertyId);
}