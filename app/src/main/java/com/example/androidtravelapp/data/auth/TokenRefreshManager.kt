package com.example.androidtravelapp.data.auth

import com.example.androidtravelapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenRefreshManager @Inject constructor(
    private val authRepository: AuthRepository,
    private val tokenManager: TokenManager
) {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    suspend fun refreshTokenIfNeeded(): Boolean {
        if (_isRefreshing.value) return true
        
        return try {
            _isRefreshing.value = true
            
            // Check if token is expired or about to expire (within 5 minutes)
            val currentToken = tokenManager.getAccessToken()
            if (currentToken == null || isTokenExpired(currentToken)) {
                val refreshToken = tokenManager.getRefreshToken()
                if (refreshToken != null) {
                    authRepository.refreshToken(refreshToken)
                    true
                } else {
                    false
                }
            } else {
                true
            }
        } finally {
            _isRefreshing.value = false
        }
    }

    private fun isTokenExpired(token: String): Boolean {
        // TODO: Implement proper JWT token expiration check
        // For now, we'll just check if the token exists
        return token.isEmpty()
    }
} 