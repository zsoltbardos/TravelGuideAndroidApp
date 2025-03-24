package com.example.androidtravelapp.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

/**
 * Data model for Destination objects returned by the API
 */
data class Destination(
    val id: Int,
    val name: String,
    val description: String,
    val location: String,
    @SerializedName("averageRating")
    val rating: Float = 0f,
    @SerializedName("reviewCount")
    val reviewCount: Int = 0,
    val pointsOfInterest: List<String> = emptyList(),
    val imageUrls: List<String> = emptyList(),
    val coverImageUrl: String = "",
    val region: String = "",
    val category: String = "",
    val price: Double = 0.0,
    val createdAt: Date? = null,
    val updatedAt: Date? = null
) {
    // Convenience property to get the first image URL for card displays
    val imageUrl: String
        get() = coverImageUrl.ifEmpty { imageUrls.firstOrNull() ?: "" }
}

/**
 * Data model for creating or updating a destination
 */
data class DestinationRequest(
    val name: String,
    val description: String,
    val location: String,
    val pointsOfInterest: List<String>,
    val imageUrls: List<String>,
    val coverImageUrl: String,
    val region: String,
    val category: String,
    val price: Double
) 