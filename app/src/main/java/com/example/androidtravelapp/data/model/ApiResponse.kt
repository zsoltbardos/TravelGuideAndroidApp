package com.example.androidtravelapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Generic API response wrapper as defined in API documentation
 */
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T?,
    val errors: List<String>?
)

/**
 * Paginated response structure for list endpoints
 */
data class PaginatedResponse<T>(
    val items: List<T>,
    @SerializedName("totalCount")
    val totalItems: Int,
    val pageNumber: Int,
    val pageSize: Int,
    val totalPages: Int,
    val hasPrevious: Boolean,
    val hasNext: Boolean
) 