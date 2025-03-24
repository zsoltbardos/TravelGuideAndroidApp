package com.example.androidtravelapp.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidtravelapp.data.model.Destination
import com.example.androidtravelapp.data.model.Review
import com.example.androidtravelapp.data.model.ReviewRequest
import com.example.androidtravelapp.data.repository.DestinationRepository
import com.example.androidtravelapp.data.repository.ReviewRepository
import com.example.androidtravelapp.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DestinationDetailsViewModel @Inject constructor(
    private val destinationRepository: DestinationRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    
    // UI state for the selected destination
    private val _destination = MutableStateFlow<Destination?>(null)
    val destination: StateFlow<Destination?> = _destination.asStateFlow()
    
    // UI state for reviews
    private val _reviews = MutableStateFlow<List<Review>>(emptyList())
    val reviews: StateFlow<List<Review>> = _reviews.asStateFlow()
    
    // Loading states
    private val _isLoadingDestination = MutableStateFlow(false)
    val isLoadingDestination: StateFlow<Boolean> = _isLoadingDestination.asStateFlow()
    
    private val _isLoadingReviews = MutableStateFlow(false)
    val isLoadingReviews: StateFlow<Boolean> = _isLoadingReviews.asStateFlow()
    
    private val _isSubmittingReview = MutableStateFlow(false)
    val isSubmittingReview: StateFlow<Boolean> = _isSubmittingReview.asStateFlow()
    
    // Error states
    private val _destinationError = MutableStateFlow<String?>(null)
    val destinationError: StateFlow<String?> = _destinationError.asStateFlow()
    
    private val _reviewsError = MutableStateFlow<String?>(null)
    val reviewsError: StateFlow<String?> = _reviewsError.asStateFlow()
    
    private val _reviewSubmissionError = MutableStateFlow<String?>(null)
    val reviewSubmissionError: StateFlow<String?> = _reviewSubmissionError.asStateFlow()
    
    // Success state for review submission
    private val _reviewSubmissionSuccess = MutableStateFlow(false)
    val reviewSubmissionSuccess: StateFlow<Boolean> = _reviewSubmissionSuccess.asStateFlow()
    
    fun loadDestination(destinationId: Int) {
        viewModelScope.launch {
            _isLoadingDestination.value = true
            _destinationError.value = null
            
            when (val result = destinationRepository.getDestinationById(destinationId)) {
                is NetworkResult.Success -> {
                    _destination.value = result.data
                }
                is NetworkResult.Error -> {
                    _destinationError.value = result.message
                }
                is NetworkResult.Exception -> {
                    _destinationError.value = result.throwable.message ?: "Unknown error occurred"
                }
                is NetworkResult.Loading -> {
                    // Loading is handled via the _isLoadingDestination state
                }
            }
            
            _isLoadingDestination.value = false
        }
    }
    
    fun loadReviews(destinationId: Int, pageNumber: Int = 1, pageSize: Int = 10) {
        viewModelScope.launch {
            _isLoadingReviews.value = true
            _reviewsError.value = null
            
            when (val result = reviewRepository.getReviewsForDestination(
                destinationId = destinationId,
                pageNumber = pageNumber,
                pageSize = pageSize
            )) {
                is NetworkResult.Success -> {
                    val paginatedResponse = result.data
                    // Replace existing reviews when loading first page, or append when loading more
                    if (pageNumber == 1) {
                        _reviews.value = paginatedResponse.items
                    } else {
                        _reviews.value = _reviews.value + paginatedResponse.items
                    }
                }
                is NetworkResult.Error -> {
                    _reviewsError.value = result.message
                }
                is NetworkResult.Exception -> {
                    _reviewsError.value = result.throwable.message ?: "Unknown error occurred"
                }
                is NetworkResult.Loading -> {
                    // Loading is handled via the _isLoadingReviews state
                }
            }
            
            _isLoadingReviews.value = false
        }
    }
    
    fun submitReview(destinationId: Int, reviewerName: String, comment: String, rating: Float) {
        viewModelScope.launch {
            _isSubmittingReview.value = true
            _reviewSubmissionError.value = null
            _reviewSubmissionSuccess.value = false
            
            val reviewRequest = ReviewRequest(
                reviewerName = reviewerName,
                comment = comment,
                rating = rating
            )
            
            when (val result = reviewRepository.addReviewForDestination(destinationId, reviewRequest)) {
                is NetworkResult.Success -> {
                    // Add the new review to the list (prepend it)
                    _reviews.value = listOf(result.data) + _reviews.value
                    _reviewSubmissionSuccess.value = true
                    
                    // Reload the destination to get the updated rating
                    loadDestination(destinationId)
                }
                is NetworkResult.Error -> {
                    _reviewSubmissionError.value = result.message
                }
                is NetworkResult.Exception -> {
                    _reviewSubmissionError.value = result.throwable.message ?: "Unknown error occurred"
                }
                is NetworkResult.Loading -> {
                    // Loading is handled via the _isSubmittingReview state
                }
            }
            
            _isSubmittingReview.value = false
        }
    }
    
    fun resetReviewSubmission() {
        _reviewSubmissionSuccess.value = false
        _reviewSubmissionError.value = null
    }
} 