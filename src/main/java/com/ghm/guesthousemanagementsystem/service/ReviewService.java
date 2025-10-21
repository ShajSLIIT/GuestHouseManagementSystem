package com.naveen.guesthousemanagementsystem.service;


import com.naveen.guesthousemanagementsystem.dto.review.ReviewRequestDTO;
import com.naveen.guesthousemanagementsystem.dto.review.ReviewResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ReviewService {

    // ====================== ADMIN METHODS ======================
    List<ReviewResponseDTO> getAllReviews();

    List<ReviewResponseDTO> getReviewsByPropertyId(UUID propertyId);

    ReviewResponseDTO getReviewByReferenceId(String referenceId);

    List<ReviewResponseDTO> searchReviewsByGuestName(String guestName);

    void deleteReviewByReferenceId(String referenceId);

    // ====================== GUEST METHODS ======================
    ReviewResponseDTO createReview(ReviewRequestDTO reviewRequestDTO);

    ReviewResponseDTO getMyReviewByReferenceId(String referenceId);

    ReviewResponseDTO updateMyReviewByReferenceId(String referenceId, ReviewRequestDTO requestDTO);

    void deleteMyReviewByReferenceId(String referenceId);

    // ====================== PROPERTY RATING METHODS ======================
    PropertyRatingResponse getPropertyRating(UUID propertyId);

    PropertyRatingResponse getPropertyRatingByBookingReferenceId(String referenceId);

    List<PropertyRatingResponse> getAllPropertyRatings();

    ReviewStatistics getReviewStatistics();

    // ====================== INNER CLASSES ======================
    record ReviewStatistics(int totalReviews, double averageRating, long fiveStar, long fourStar, long threeStar,
                            long twoStar, long oneStar) {
    }

    record PropertyRatingResponse(UUID propertyId, double averageRating, long totalReviews, long fiveStar,
                                  long fourStar, long threeStar, long twoStar, long oneStar) {
    }
}