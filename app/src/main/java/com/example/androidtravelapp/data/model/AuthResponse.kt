package com.example.androidtravelapp.data.model

import com.google.gson.annotations.SerializedName

/**
 * Data model for authentication response from API
 */
data class AuthResponse(
    val accessToken: String,
    val refreshToken: String,
    val expiration: Long,
    val userData: UserData
)

/**
 * Data model for user data
 */
data class UserData(
    val id: Int,
    val username: String,
    val email: String,
    val firstName: String,
    val lastName: String
)

/**
 * Data model for login request
 */
data class LoginRequest(
    val username: String,
    val password: String
)

/**
 * Data model for registration request
 */
data class RegisterRequest(
    val username: String,
    val email: String,
    val password: String,
    val firstName: String,
    val lastName: String
)

/**
 * Data model for refresh token request
 */
data class RefreshTokenRequest(
    val refreshToken: String
) 