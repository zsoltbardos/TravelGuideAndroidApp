package com.example.androidtravelapp.data.auth

import com.example.androidtravelapp.data.model.UserData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages authentication state across the app
 */
@Singleton
class AuthState @Inject constructor(
    private val tokenManager: TokenManager,
    private val tokenRefreshManager: TokenRefreshManager
) {
    private val _isLoggedIn = MutableStateFlow(tokenManager.isLoggedIn())
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _userData = MutableStateFlow(tokenManager.getUserData())
    val userData: StateFlow<UserData?> = _userData.asStateFlow()

    fun updateAuthState() {
        _isLoggedIn.value = tokenManager.isLoggedIn()
        _userData.value = tokenManager.getUserData()
    }

    fun updateLoginState(isLoggedIn: Boolean) {
        _isLoggedIn.value = isLoggedIn
    }

    suspend fun logout() {
        tokenManager.clearAuthData()
        _isLoggedIn.value = false
        _userData.value = null
    }

    suspend fun refreshTokenIfNeeded(): Boolean {
        return tokenRefreshManager.refreshTokenIfNeeded()
    }
} 