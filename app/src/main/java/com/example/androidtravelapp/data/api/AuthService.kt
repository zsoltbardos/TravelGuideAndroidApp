package com.example.androidtravelapp.data.api

import com.example.androidtravelapp.data.model.ApiResponse
import com.example.androidtravelapp.data.model.AuthResponse
import com.example.androidtravelapp.data.model.LoginRequest
import com.example.androidtravelapp.data.model.RefreshTokenRequest
import com.example.androidtravelapp.data.model.RegisterRequest
import retrofit2.http.*

interface AuthService {
    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): ApiResponse<AuthResponse>

    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): ApiResponse<AuthResponse>

    @POST("api/auth/refresh-token")
    suspend fun refreshToken(
        @Body refreshTokenRequest: RefreshTokenRequest
    ): ApiResponse<AuthResponse>
} 