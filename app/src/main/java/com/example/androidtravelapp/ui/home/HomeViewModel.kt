package com.example.androidtravelapp.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtravelapp.data.model.Destination
import com.example.androidtravelapp.data.repository.DestinationRepository
import com.example.androidtravelapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository
) : ViewModel() {
    
    private val _destinations = MutableStateFlow<List<Destination>>(emptyList())
    val destinations: StateFlow<List<Destination>> = _destinations.asStateFlow()
    
    private val _popularDestinations = MutableStateFlow<List<Destination>>(emptyList())
    val popularDestinations: StateFlow<List<Destination>> = _popularDestinations.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Current active search term
    private val _activeSearchTerm = MutableStateFlow("")
    val activeSearchTerm: StateFlow<String> = _activeSearchTerm.asStateFlow()
    
    private var currentPage = 1
    private var isLastPage = false
    private var searchJob: Job? = null
    
    init {
        refreshDestinations()
        loadPopularDestinations()
    }
    
    fun refreshDestinations() {
        _error.value = null
        currentPage = 1
        isLastPage = false
        loadDestinations(isRefresh = true)
    }
    
    fun loadMoreDestinations() {
        if (!_isLoading.value && !isLastPage) {
            currentPage++
            loadDestinations(isRefresh = false)
        }
    }
    
    private fun loadDestinations(isRefresh: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            
            val searchTerm = _activeSearchTerm.value.trim()
            val result = if (searchTerm.isEmpty()) {
                destinationRepository.getDestinations(page = currentPage)
            } else {
                destinationRepository.searchDestinations(searchTerm, page = currentPage)
            }
            
            when (result) {
                is NetworkResult.Success -> {
                    val newDestinations = result.data
                    
                    // Since we're always getting the same destinations due to API limitations,
                    // we need to prevent duplicates and infinite pagination.
                    // Check if we got any new unique destinations
                    val existingIds = _destinations.value.map { it.id }
                    val uniqueNewDestinations = newDestinations.filter { it.id !in existingIds }
                    
                    // If no new unique destinations, we're at the end
                    if (uniqueNewDestinations.isEmpty()) {
                        isLastPage = true
                    }
                    
                    if (isRefresh) {
                        _destinations.value = newDestinations
                    } else {
                        // Only add unique destinations
                        _destinations.value = _destinations.value + uniqueNewDestinations
                    }
                    _error.value = null
                }
                is NetworkResult.Error -> {
                    if (isRefresh) {
                        _destinations.value = emptyList()
                    }
                    _error.value = result.message
                }
                is NetworkResult.Exception -> {
                    if (isRefresh) {
                        _destinations.value = emptyList()
                    }
                    _error.value = result.throwable.message ?: "Unknown error occurred"
                }
                is NetworkResult.Loading -> {
                    // Loading is handled via the _isLoading state
                }
            }
            
            _isLoading.value = false
        }
    }
    
    private fun loadPopularDestinations() {
        viewModelScope.launch {
            when (val result = destinationRepository.getPopularDestinations()) {
                is NetworkResult.Success -> {
                    _popularDestinations.value = result.data
                }
                is NetworkResult.Error -> {
                    // We don't set the error for popular destinations to avoid
                    // blocking the whole screen if only this part fails
                    _popularDestinations.value = emptyList()
                }
                is NetworkResult.Exception -> {
                    // We don't set the error for popular destinations to avoid
                    // blocking the whole screen if only this part fails
                    _popularDestinations.value = emptyList()
                }
                is NetworkResult.Loading -> {
                    // Loading state is handled elsewhere
                }
            }
        }
    }
    
    // Update the search query without triggering a search
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    // Execute the search with the current query
    fun executeSearch() {
        _activeSearchTerm.value = _searchQuery.value
        refreshDestinations()
    }
    
    // Debounced search - will wait for the specified delay before executing
    fun debouncedSearch(query: String, delayMillis: Long = 500) {
        _searchQuery.value = query
        
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(delayMillis)
            _activeSearchTerm.value = query
            refreshDestinations()
        }
    }
} 