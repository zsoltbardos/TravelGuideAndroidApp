package com.example.androidtravelapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Data model for reviews received from the API
 */
data class Review(
    val id: Int,
    val destinationId: Int,
    val rating: Float,
    val reviewerName: String,
    @SerializedName("reviewText")
    val comment: String,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
)

/**
 * Data model for submitting a new review
 */
data class ReviewRequest(
    val rating: Float,
    val reviewerName: String,
    @SerializedName("reviewText")
    val comment: String
)

/**
 * Data model for updating an existing review
 */
data class ReviewUpdateRequest(
    val reviewText: String,
    val rating: Float
) 