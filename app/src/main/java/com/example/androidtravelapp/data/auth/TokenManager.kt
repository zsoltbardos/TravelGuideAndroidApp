package com.example.androidtravelapp.data.auth

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.androidtravelapp.data.model.UserData
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages JWT tokens for authentication with the API
 */
@Singleton
class TokenManager @Inject constructor(
    context: Context
) {
    companion object {
        private const val PREFS_NAME = "secure_travel_prefs"
        private const val KEY_ACCESS_TOKEN = "access_token"
        private const val KEY_REFRESH_TOKEN = "refresh_token"
        private const val KEY_EXPIRATION = "expiration"
        private const val KEY_USER_DATA = "user_data"
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
    }
    
    private val gson = Gson()
    
    // Set up EncryptedSharedPreferences for secure storage
    private val prefs: SharedPreferences = try {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()
        
        EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    } catch (e: Exception) {
        // Fallback to regular SharedPreferences if encryption fails
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }
    
    /**
     * Save all authentication data
     */
    fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        expiration: Long,
        userData: UserData
    ) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putLong(KEY_EXPIRATION, expiration)
            .putString(KEY_USER_DATA, gson.toJson(userData))
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .apply()
    }
    
    /**
     * Update tokens
     */
    fun updateTokens(accessToken: String, refreshToken: String, expiration: Long) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            .putLong(KEY_EXPIRATION, expiration)
            .apply()
    }
    
    /**
     * Get the stored access token
     */
    fun getAccessToken(): String? {
        return prefs.getString(KEY_ACCESS_TOKEN, null)
    }
    
    /**
     * Get the stored refresh token
     */
    fun getRefreshToken(): String? {
        return prefs.getString(KEY_REFRESH_TOKEN, null)
    }
    
    /**
     * Get the token expiration timestamp
     */
    fun getTokenExpiration(): Long {
        return prefs.getLong(KEY_EXPIRATION, 0)
    }
    
    /**
     * Get the stored user data
     */
    fun getUserData(): UserData? {
        val userDataJson = prefs.getString(KEY_USER_DATA, null) ?: return null
        return try {
            gson.fromJson(userDataJson, UserData::class.java)
        } catch (e: Exception) {
            null
        }
    }
    
    /**
     * Check if user is logged in
     */
    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }
    
    /**
     * Check if the access token is expired
     */
    fun isTokenExpired(): Boolean {
        val expiration = getTokenExpiration()
        return expiration > 0 && System.currentTimeMillis() > expiration
    }
    
    /**
     * Clear all auth data (logout)
     */
    fun clearAuthData() {
        prefs.edit()
            .remove(KEY_ACCESS_TOKEN)
            .remove(KEY_REFRESH_TOKEN)
            .remove(KEY_EXPIRATION)
            .remove(KEY_USER_DATA)
            .putBoolean(KEY_IS_LOGGED_IN, false)
            .apply()
    }
} 