package com.example.androidtravelapp.data.api

import com.example.androidtravelapp.data.model.ApiResponse
import com.example.androidtravelapp.data.model.Destination
import com.example.androidtravelapp.data.model.DestinationRequest
import com.example.androidtravelapp.data.model.PaginatedResponse
import retrofit2.http.*

interface DestinationService {
    @GET("api/destinations")
    suspend fun getAllDestinations(
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): ApiResponse<PaginatedResponse<Destination>>

    @GET("api/destinations/{id}")
    suspend fun getDestinationById(
        @Path("id") id: Int
    ): ApiResponse<Destination>

    @POST("api/destinations")
    suspend fun createDestination(
        @Body destinationRequest: DestinationRequest
    ): ApiResponse<Destination>

    @PUT("api/destinations/{id}")
    suspend fun updateDestination(
        @Path("id") id: Int,
        @Body destinationRequest: DestinationRequest
    ): ApiResponse<Destination>

    @DELETE("api/destinations/{id}")
    suspend fun deleteDestination(
        @Path("id") id: Int
    ): ApiResponse<Boolean>

    @GET("api/destinations/search")
    suspend fun searchDestinations(
        @Query("q") query: String? = null,
        @Query("region") region: String? = null,
        @Query("category") category: String? = null,
        @Query("minRating") minRating: Float? = null,
        @Query("pageNumber") pageNumber: Int? = null,
        @Query("pageSize") pageSize: Int? = null,
        @Query("sortBy") sortBy: String? = null,
        @Query("sortOrder") sortOrder: String? = null
    ): ApiResponse<PaginatedResponse<Destination>>

    @GET("api/destinations/popular")
    suspend fun getPopularDestinations(
        @Query("count") count: Int? = null
    ): ApiResponse<List<Destination>>
} 