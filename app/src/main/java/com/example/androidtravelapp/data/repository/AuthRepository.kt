package com.example.androidtravelapp.data.repository

import com.example.androidtravelapp.data.api.AuthService
import com.example.androidtravelapp.data.auth.TokenManager
import com.example.androidtravelapp.data.model.AuthResponse
import com.example.androidtravelapp.data.model.LoginRequest
import com.example.androidtravelapp.data.model.RefreshTokenRequest
import com.example.androidtravelapp.data.model.RegisterRequest
import com.example.androidtravelapp.data.model.UserData
import com.example.androidtravelapp.util.NetworkResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: AuthService
) {
    // Mock user data for fallback
    private val mockUsers = listOf(
        UserData(
            id = 1,
            username = "demo",
            email = "demo@example.com",
            firstName = "Demo",
            lastName = "User"
        )
    )
    
    suspend fun login(username: String, password: String): NetworkResult<AuthResponse> {
        return try {
            val loginRequest = LoginRequest(username, password)
            val response = authService.login(loginRequest)
            
            if (response.success && response.data != null) {
                // Save tokens and user data
                tokenManager.saveAuthData(
                    accessToken = response.data.accessToken,
                    refreshToken = response.data.refreshToken,
                    expiration = response.data.expiration,
                    userData = response.data.userData
                )
                
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Login failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun register(registerRequest: RegisterRequest): NetworkResult<AuthResponse> {
        return try {
            val response = authService.register(registerRequest)
            
            if (response.success && response.data != null) {
                // Save tokens and user data
                tokenManager.saveAuthData(
                    accessToken = response.data.accessToken,
                    refreshToken = response.data.refreshToken,
                    expiration = response.data.expiration,
                    userData = response.data.userData
                )
                
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Registration failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun refreshToken(refreshToken: String): NetworkResult<AuthResponse> {
        return try {
            val refreshTokenRequest = RefreshTokenRequest(refreshToken)
            val response = authService.refreshToken(refreshTokenRequest)
            
            if (response.success && response.data != null) {
                // Update tokens but keep the same user data
                tokenManager.updateTokens(
                    accessToken = response.data.accessToken,
                    refreshToken = response.data.refreshToken,
                    expiration = response.data.expiration
                )
                
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Token refresh failed")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    fun logout() {
        tokenManager.clearAuthData()
    }
    
    fun isLoggedIn(): Boolean {
        return tokenManager.isLoggedIn()
    }
    
    fun getUserId(): Int? {
        val userData = tokenManager.getUserData()
        return userData?.id
    }
    
    fun getUsername(): String? {
        val userData = tokenManager.getUserData()
        return userData?.username
    }
} 