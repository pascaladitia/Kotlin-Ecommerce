package com.pascal.model.response

data class ReviewRating(
    val id: String,
    val userId: String,
    val productId: String,
    val reviewText: String,
    val rating: Int
)