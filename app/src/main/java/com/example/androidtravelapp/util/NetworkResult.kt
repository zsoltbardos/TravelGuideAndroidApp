package com.example.androidtravelapp.util

/**
 * Sealed class for handling network responses.
 * This provides a way to handle success, error, and exception states in a type-safe way.
 */
sealed class NetworkResult<out T> {
    data class Success<out T>(val data: T) : NetworkResult<T>()
    data class Error(val message: String, val errors: List<String>? = null) : NetworkResult<Nothing>()
    data class Exception(val throwable: Throwable) : NetworkResult<Nothing>()
    data class Loading<out T>(val data: T? = null) : NetworkResult<T>()
    
    /**
     * Check if the result is a success
     */
    fun isSuccess(): Boolean = this is Success
    
    /**
     * Get data if the result is successful, otherwise null
     */
    fun getOrNull(): T? = when (this) {
        is Success -> data
        is Loading -> data
        else -> null
    }
    
    /**
     * Transform the data if the result is successful
     */
    fun <R> map(transform: (T) -> R): NetworkResult<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
        is Exception -> this
        is Loading -> Loading(data?.let { transform(it) })
    }
} 