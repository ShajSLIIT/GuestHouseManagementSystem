package com.naveen.guesthousemanagementsystem.service.impl;


import com.naveen.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.review.ReviewResponseDTO;
import com.naveen.guesthousemanagementsystem.entity.Booking;
import com.naveen.guesthousemanagementsystem.entity.Review;
import com.naveen.guesthousemanagementsystem.exeption.ResourceNotFoundException;
import com.naveen.guesthousemanagementsystem.mapper.ReviewMapper;
import com.naveen.guesthousemanagementsystem.repository.BookingRepository;
import com.naveen.guesthousemanagementsystem.repository.ReviewRepository;
import com.naveen.guesthousemanagementsystem.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final BookingRepository bookingRepository;

    // ====================== ADMIN METHODS ======================

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> getReviewsByPropertyId(UUID propertyId) {
        return reviewRepository.findByBookingPropertyId(propertyId).stream()
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getReviewByReferenceId(String referenceId) {
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + referenceId));

        Review review = reviewRepository.findFirstByBookingId(booking.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("No review found for booking with reference ID: " + referenceId));

        return reviewMapper.toResponseDTO(review);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReviewResponseDTO> searchReviewsByGuestName(String guestName) {
        return reviewRepository.findAll().stream()
                .filter(review -> review.getBooking().getGuestName().toLowerCase().contains(guestName.toLowerCase()))
                .map(reviewMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteReviewByReferenceId(String referenceId) {
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + referenceId));

        Review review = reviewRepository.findFirstByBookingId(booking.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("No review found for booking with reference ID: " + referenceId));

        reviewRepository.delete(review);
    }


    // ====================== GUEST METHODS ======================

    @Override
    public ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO) {
        Booking booking = bookingRepository.findByReferenceId(reviewRequestDTO.getReferenceId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + reviewRequestDTO.getReferenceId()));

        validateReviewEligibility(booking);

        if (reviewRepository.existsByBookingId(booking.getBookingId())) {
            throw new IllegalArgumentException("Review already exists for this booking");
        }

        if (reviewRequestDTO.getRating() == null || reviewRequestDTO.getRating() < 1 || reviewRequestDTO.getRating() > 5) {
            throw new IllegalArgumentException("Rating must be between 1-5");
        }

        Review review = reviewMapper.toEntity(reviewRequestDTO, booking);
        Review savedReview = reviewRepository.save(review);

        return reviewMapper.toResponseDTO(savedReview);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewResponseDTO getMyReviewByReferenceId(String referenceId) {
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + referenceId));

        Review review = reviewRepository.findFirstByBookingId(booking.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("No review found for your booking"));

        return reviewMapper.toResponseDTO(review);
    }

    @Override
    public ReviewResponseDTO updateMyReviewByReferenceId(String referenceId, ReviewRequestDTO requestDTO) {
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + referenceId));

        Review existingReview = reviewRepository.findFirstByBookingId(booking.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("No review found for your booking"));

        if (requestDTO.getRating() != null && (requestDTO.getRating() < 1 || requestDTO.getRating() > 5)) {
            throw new IllegalArgumentException("Rating must be between 1-5");
        }

        reviewMapper.updateEntityFromDTO(requestDTO, existingReview);
        Review updatedReview = reviewRepository.save(existingReview);

        return reviewMapper.toResponseDTO(updatedReview);
    }

    @Override
    public void deleteMyReviewByReferenceId(String referenceId) {
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + referenceId));

        Review review = reviewRepository.findFirstByBookingId(booking.getBookingId())
                .orElseThrow(() -> new ResourceNotFoundException("No review found for your booking"));

        reviewRepository.delete(review);
    }

    // ====================== PROPERTY RATING METHODS ======================

    @Override
    @Transactional(readOnly = true)
    public PropertyRatingResponse getPropertyRating(UUID propertyId) {
        List<Review> propertyReviews = reviewRepository.findByBookingPropertyId(propertyId);

        if (propertyReviews.isEmpty()) {
            return new PropertyRatingResponse(propertyId, 0.0, 0, 0, 0, 0, 0, 0);
        }

        double averageRating = propertyReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        long totalReviews = propertyReviews.size();
        long fiveStar = propertyReviews.stream().filter(r -> r.getRating() == 5).count();
        long fourStar = propertyReviews.stream().filter(r -> r.getRating() == 4).count();
        long threeStar = propertyReviews.stream().filter(r -> r.getRating() == 3).count();
        long twoStar = propertyReviews.stream().filter(r -> r.getRating() == 2).count();
        long oneStar = propertyReviews.stream().filter(r -> r.getRating() == 1).count();

        return new PropertyRatingResponse(
                propertyId,
                Math.round(averageRating * 10.0) / 10.0,
                totalReviews,
                fiveStar, fourStar, threeStar, twoStar, oneStar
        );
    }

    @Override
    @Transactional(readOnly = true)
    public PropertyRatingResponse getPropertyRatingByBookingReferenceId(String referenceId) {
        Booking booking = bookingRepository.findByReferenceId(referenceId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with reference ID: " + referenceId));

        return getPropertyRating(booking.getProperty().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public List<PropertyRatingResponse> getAllPropertyRatings() {
        List<Review> allReviews = reviewRepository.findAll();

        return allReviews.stream()
                .collect(Collectors.groupingBy(
                        review -> review.getBooking().getProperty().getId(),
                        Collectors.toList()
                ))
                .entrySet()
                .stream()
                .map(entry -> {
                    UUID propertyId = entry.getKey();
                    List<Review> propertyReviews = entry.getValue();

                    double averageRating = propertyReviews.stream()
                            .mapToInt(Review::getRating)
                            .average()
                            .orElse(0.0);

                    long totalReviews = propertyReviews.size();
                    long fiveStar = propertyReviews.stream().filter(r -> r.getRating() == 5).count();
                    long fourStar = propertyReviews.stream().filter(r -> r.getRating() == 4).count();
                    long threeStar = propertyReviews.stream().filter(r -> r.getRating() == 3).count();
                    long twoStar = propertyReviews.stream().filter(r -> r.getRating() == 2).count();
                    long oneStar = propertyReviews.stream().filter(r -> r.getRating() == 1).count();

                    return new PropertyRatingResponse(
                            propertyId,
                            Math.round(averageRating * 10.0) / 10.0,
                            totalReviews,
                            fiveStar, fourStar, threeStar, twoStar, oneStar
                    );
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewStatistics getReviewStatistics() {
        List<Review> allReviews = reviewRepository.findAll();

        if (allReviews.isEmpty()) {
            return new ReviewStatistics(0, 0.0, 0, 0, 0, 0, 0);
        }

        double averageRating = allReviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        long fiveStar = allReviews.stream().filter(r -> r.getRating() == 5).count();
        long fourStar = allReviews.stream().filter(r -> r.getRating() == 4).count();
        long threeStar = allReviews.stream().filter(r -> r.getRating() == 3).count();
        long twoStar = allReviews.stream().filter(r -> r.getRating() == 2).count();
        long oneStar = allReviews.stream().filter(r -> r.getRating() == 1).count();

        return new ReviewStatistics(
                allReviews.size(),
                Math.round(averageRating * 10.0) / 10.0,
                fiveStar, fourStar, threeStar, twoStar, oneStar
        );
    }

    // ====================== PRIVATE HELPER METHODS ======================

    private void validateReviewEligibility(Booking booking) {
        if (booking.getStatus() != BookingStatus.confirmed) {
            throw new IllegalStateException("Can only review confirmed bookings");
        }

        if (booking.getCheckInDate().isAfter(LocalDate.now())) {
            throw new IllegalStateException("Cannot review before check-in date");
        }
    }
}