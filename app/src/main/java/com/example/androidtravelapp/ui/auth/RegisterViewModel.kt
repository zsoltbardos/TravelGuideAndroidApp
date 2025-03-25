package com.example.androidtravelapp.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtravelapp.data.model.RegisterRequest
import com.example.androidtravelapp.data.repository.AuthRepository
import com.example.androidtravelapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _isRegistered = MutableStateFlow(false)
    val isRegistered: StateFlow<Boolean> = _isRegistered.asStateFlow()
    
    fun register(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            val registerRequest = RegisterRequest(
                username = username.trim(),
                email = email.trim(),
                password = password,
                firstName = firstName.trim(),
                lastName = lastName.trim()
            )
            
            when (val result = authRepository.register(registerRequest)) {
                is NetworkResult.Success -> {
                    _isRegistered.value = true
                }
                is NetworkResult.Error -> {
                    _error.value = result.message
                }
                is NetworkResult.Loading -> {
                    // Loading state is handled by _isLoading
                }
                is NetworkResult.Exception -> {
                    _error.value = result.throwable.message ?: "Unknown error occurred"
                }
            }
            
            _isLoading.value = false
        }
    }
    
    fun clearError() {
        _error.value = null
    }
} 