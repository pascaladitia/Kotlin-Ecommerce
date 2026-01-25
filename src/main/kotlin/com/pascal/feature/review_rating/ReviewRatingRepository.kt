package com.pascal.feature.review_rating

import com.pascal.model.request.ReviewRatingRequest
import com.pascal.model.response.ReviewRating

interface ReviewRatingRepository {
    suspend fun getReviewRating(productId: String, limit: Int): List<ReviewRating>
    suspend fun addReviewRating(userId: String, reviewRating: ReviewRatingRequest): ReviewRating
    suspend fun updateReviewRating(reviewId: String, review: String, rating: Int): ReviewRating
    suspend fun deleteReviewRating(reviewId: String): String
}