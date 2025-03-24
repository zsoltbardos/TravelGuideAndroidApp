package com.example.androidtravelapp.data.repository

import com.example.androidtravelapp.data.api.ReviewService
import com.example.androidtravelapp.data.model.PaginatedResponse
import com.example.androidtravelapp.data.model.Review
import com.example.androidtravelapp.data.model.ReviewRequest
import com.example.androidtravelapp.data.model.ReviewUpdateRequest
import com.example.androidtravelapp.util.NetworkResult
import java.util.Date
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReviewRepository @Inject constructor(
    private val reviewService: ReviewService
) {
    // Mock review data can be kept for fallback if needed
    private val mockReviews = mutableListOf(
        Review(
            id = 1,
            destinationId = 1,
            rating = 5.0f,
            reviewerName = "Emma Thompson",
            comment = "Paris is absolutely magical! The Eiffel Tower at night is a must-see, and the food is phenomenal. Can't wait to visit again.",
            createdAt = Date(),
            updatedAt = Date()
        ),
        Review(
            id = 2,
            destinationId = 1,
            rating = 4.5f,
            reviewerName = "Michael Chen",
            comment = "Beautiful city with amazing architecture and culture. The museums are world-class, especially the Louvre. Only downside was the crowds during peak season.",
            createdAt = Date(),
            updatedAt = Date()
        ),
        Review(
            id = 3,
            destinationId = 2,
            rating = 5.0f,
            reviewerName = "Akiko Tanaka",
            comment = "Kyoto perfectly preserves traditional Japanese culture. The temples and gardens are peaceful and stunning. Highly recommend visiting during cherry blossom season.",
            createdAt = Date(),
            updatedAt = Date()
        ),
        Review(
            id = 4,
            destinationId = 2,
            rating = 4.8f,
            reviewerName = "David Wilson",
            comment = "A beautiful contrast to Tokyo's modernity. The bamboo grove is otherworldly, and the cuisine is exquisite. Take time to explore the smaller temples too.",
            createdAt = Date(),
            updatedAt = Date()
        ),
        Review(
            id = 5,
            destinationId = 3,
            rating = 5.0f,
            reviewerName = "Sofia Garcia",
            comment = "Santorini's views are even more breathtaking than in photos. Watching the sunset in Oia is an unforgettable experience. Worth every penny!",
            createdAt = Date(),
            updatedAt = Date()
        ),
        Review(
            id = 6,
            destinationId = 4,
            rating = 4.9f,
            reviewerName = "James Rodriguez",
            comment = "Machu Picchu exceeded all my expectations. The hike along the Inca Trail was challenging but incredibly rewarding. The ancient ruins are spectacular.",
            createdAt = Date(),
            updatedAt = Date()
        ),
        Review(
            id = 7,
            destinationId = 5,
            rating = 5.0f,
            reviewerName = "Olivia Mukasa",
            comment = "The Serengeti safari was the trip of a lifetime. We saw the Big Five in just two days! The vast plains and wildlife are simply magnificent.",
            createdAt = Date(),
            updatedAt = Date()
        )
    )
    
    suspend fun getReviewsForDestination(
        destinationId: Int,
        pageNumber: Int = 1,
        pageSize: Int = 10
    ): NetworkResult<PaginatedResponse<Review>> {
        return try {
            // Omit pagination parameters to avoid potential 500 errors
            val response = reviewService.getReviewsForDestination(
                destinationId = destinationId,
                pageNumber = null,
                pageSize = null,
                sortBy = null,
                sortOrder = null
            )
            
            if (response.success && response.data != null) {
                // Use the paginated response as-is since it's correctly structured
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun getReviewById(reviewId: Int): NetworkResult<Review> {
        return try {
            val response = reviewService.getReviewById(reviewId)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun addReviewForDestination(
        destinationId: Int,
        reviewRequest: ReviewRequest
    ): NetworkResult<Review> {
        return try {
            val response = reviewService.addReviewForDestination(destinationId, reviewRequest)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun updateReview(
        reviewId: Int,
        reviewUpdateRequest: ReviewUpdateRequest
    ): NetworkResult<Review> {
        return try {
            val response = reviewService.updateReview(reviewId, reviewUpdateRequest)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun deleteReview(reviewId: Int): NetworkResult<Boolean> {
        return try {
            val response = reviewService.deleteReview(reviewId)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
} 