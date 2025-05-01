package com.example.androidtravelapp.data.repository

import com.example.androidtravelapp.data.api.AuthService
import com.example.androidtravelapp.data.auth.TokenManager
import com.example.androidtravelapp.data.model.ApiResponse
import com.example.androidtravelapp.data.model.AuthResponse
import com.example.androidtravelapp.data.model.LoginRequest
import com.example.androidtravelapp.data.model.RefreshTokenRequest
import com.example.androidtravelapp.data.model.RegisterRequest
import com.example.androidtravelapp.data.model.UserData
import com.example.androidtravelapp.util.NetworkResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException
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
    
    suspend fun login(email: String, password: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.login(LoginRequest(email, password))
                if (response.success && response.data != null) {
                    // Set a fixed expiration time of 2 hours
                    val expiration = System.currentTimeMillis() + (2 * 60 * 60 * 1000)
                    val userData = UserData(
                        id = response.data.userId,
                        username = response.data.username,
                        email = email,
                        firstName = response.data.username, // Using username as firstName since it's not provided
                        lastName = "" // Empty lastName since it's not provided
                    )
                    tokenManager.saveAuthData(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken,
                        expiration = expiration,
                        userData = userData
                    )
                    NetworkResult.Success(response.data)
                } else {
                    // Use the error message from the API response if available
                    val errorMessage = response.message ?: "Login failed"
                    val errors = response.errors
                    NetworkResult.Error(errorMessage, errors)
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> NetworkResult.Error("Invalid Credentials")
                    401 -> NetworkResult.Error("Invalid Credentials")
                    403 -> NetworkResult.Error("Account is locked")
                    404 -> NetworkResult.Error("Account not found")
                    429 -> NetworkResult.Error("Too many login attempts. Please try again later")
                    else -> NetworkResult.Error("Server error: ${e.message()}")
                }
            } catch (e: IOException) {
                NetworkResult.Error("Network error: Please check your internet connection")
            } catch (e: Exception) {
                NetworkResult.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }
    
    suspend fun register(request: RegisterRequest): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.register(request)
                if (response.success && response.data != null) {
                    // Set a fixed expiration time of 2 hours
                    val expiration = System.currentTimeMillis() + (2 * 60 * 60 * 1000)
                    val userData = UserData(
                        id = response.data.userId,
                        username = response.data.username,
                        email = request.email,
                        firstName = request.firstName,
                        lastName = request.lastName
                    )
                    tokenManager.saveAuthData(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken,
                        expiration = expiration,
                        userData = userData
                    )
                    NetworkResult.Success(response.data)
                } else {
                    // Use the error message from the API response if available
                    val errorMessage = response.message ?: "Registration failed"
                    val errors = response.errors
                    NetworkResult.Error(errorMessage, errors)
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    400 -> NetworkResult.Error("Invalid registration data")
                    409 -> NetworkResult.Error("Email or username already exists")
                    422 -> NetworkResult.Error("Invalid input data")
                    else -> NetworkResult.Error("Server error: ${e.message()}")
                }
            } catch (e: IOException) {
                NetworkResult.Error("Network error: Please check your internet connection")
            } catch (e: Exception) {
                NetworkResult.Error("An unexpected error occurred: ${e.message}")
            }
        }
    }
    
    suspend fun refreshToken(refreshToken: String): NetworkResult<AuthResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val response = authService.refreshToken(RefreshTokenRequest(refreshToken))
                if (response.success && response.data != null) {
                    // Set a fixed expiration time of 2 hours
                    val expiration = System.currentTimeMillis() + (2 * 60 * 60 * 1000)
                    val userData = UserData(
                        id = response.data.userId,
                        username = response.data.username,
                        email = "", // Empty email since it's not provided in refresh token response
                        firstName = response.data.username, // Using username as firstName since it's not provided
                        lastName = "" // Empty lastName since it's not provided
                    )
                    tokenManager.saveAuthData(
                        accessToken = response.data.accessToken,
                        refreshToken = response.data.refreshToken,
                        expiration = expiration,
                        userData = userData
                    )
                    NetworkResult.Success(response.data)
                } else {
                    // Use the error message from the API response if available
                    val errorMessage = response.message ?: "Token refresh failed"
                    val errors = response.errors
                    NetworkResult.Error(errorMessage, errors)
                }
            } catch (e: HttpException) {
                when (e.code()) {
                    401 -> NetworkResult.Error("Invalid refresh token")
                    403 -> NetworkResult.Error("Refresh token expired")
                    else -> NetworkResult.Error("Server error: ${e.message()}")
                }
            } catch (e: IOException) {
                NetworkResult.Error("Network error: Please check your internet connection")
            } catch (e: Exception) {
                NetworkResult.Error("An unexpected error occurred: ${e.message}")
            }
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