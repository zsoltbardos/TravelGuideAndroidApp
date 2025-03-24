package com.example.androidtravelapp.data.api

import com.example.androidtravelapp.data.model.ApiResponse
import com.example.androidtravelapp.data.model.PaginatedResponse
import com.example.androidtravelapp.data.model.Review
import com.example.androidtravelapp.data.model.ReviewRequest
import com.example.androidtravelapp.data.model.ReviewUpdateRequest
import retrofit2.http.*

interface ReviewService {
    @GET("api/reviews")
    suspend fun getAllReviews(
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): ApiResponse<PaginatedResponse<Review>>

    @GET("api/reviews/{id}")
    suspend fun getReviewById(
        @Path("id") id: Int
    ): ApiResponse<Review>

    @GET("api/destinations/{destinationId}/reviews")
    suspend fun getReviewsForDestination(
        @Path("destinationId") destinationId: Int,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): ApiResponse<PaginatedResponse<Review>>

    @POST("api/destinations/{destinationId}/reviews")
    suspend fun addReviewForDestination(
        @Path("destinationId") destinationId: Int,
        @Body reviewRequest: ReviewRequest
    ): ApiResponse<Review>

    @PUT("api/reviews/{id}")
    suspend fun updateReview(
        @Path("id") id: Int,
        @Body reviewUpdateRequest: ReviewUpdateRequest
    ): ApiResponse<Review>

    @DELETE("api/reviews/{id}")
    suspend fun deleteReview(
        @Path("id") id: Int
    ): ApiResponse<Boolean>
} 