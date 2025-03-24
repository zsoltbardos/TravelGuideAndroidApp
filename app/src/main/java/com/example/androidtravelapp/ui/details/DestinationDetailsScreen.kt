package com.example.androidtravelapp.ui.details

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.androidtravelapp.R
import com.example.androidtravelapp.data.model.Review
import com.example.androidtravelapp.ui.common.ErrorMessage
import com.example.androidtravelapp.ui.common.LoadingIndicator
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailsScreen(
    destinationId: Int,
    onBackClick: () -> Unit,
    currentLocale: Locale = Locale.getDefault(),
    viewModel: DestinationDetailsViewModel = hiltViewModel()
) {
    // Observe data from ViewModel
    val destination by viewModel.destination.collectAsStateWithLifecycle()
    val reviews by viewModel.reviews.collectAsStateWithLifecycle()
    val isLoadingDestination by viewModel.isLoadingDestination.collectAsStateWithLifecycle()
    val isLoadingReviews by viewModel.isLoadingReviews.collectAsStateWithLifecycle()
    val isSubmittingReview by viewModel.isSubmittingReview.collectAsStateWithLifecycle()
    val destinationError by viewModel.destinationError.collectAsStateWithLifecycle()
    val reviewsError by viewModel.reviewsError.collectAsStateWithLifecycle()
    val reviewSubmissionError by viewModel.reviewSubmissionError.collectAsStateWithLifecycle()
    val reviewSubmissionSuccess by viewModel.reviewSubmissionSuccess.collectAsStateWithLifecycle()
    
    // Trigger data loading
    LaunchedEffect(destinationId) {
        viewModel.loadDestination(destinationId)
        viewModel.loadReviews(destinationId)
    }
    
    // UI state for review submission
    var showReviewDialog by remember { mutableStateOf(false) }
    var reviewText by remember { mutableStateOf("") }
    var reviewRating by remember { mutableStateOf(5) }
    var reviewerName by remember { mutableStateOf("") }
    
    // Reset review dialog when successfully submitted
    LaunchedEffect(reviewSubmissionSuccess) {
        if (reviewSubmissionSuccess) {
            showReviewDialog = false
            reviewText = ""
            reviewRating = 5
            reviewerName = ""
            viewModel.resetReviewSubmission()
        }
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(destination?.name ?: stringResource(R.string.loading)) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoadingDestination && destination == null) {
                LoadingIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (destinationError != null) {
                ErrorMessage(
                    message = destinationError ?: stringResource(R.string.destination_not_found),
                    onRetry = { viewModel.loadDestination(destinationId) },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (destination != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Destination image
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(destination?.imageUrl)
                            .crossfade(true)
                            .placeholder(R.drawable.placeholder_image)
                            .error(R.drawable.error_image)
                            .build(),
                        contentDescription = destination?.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                    )
                    
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        // Destination Title and Location
                        Text(
                            text = destination?.name ?: "",
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = destination?.location ?: "",
                            fontSize = 18.sp,
                            color = Color.Gray
                        )
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Rating Section
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = stringResource(R.string.rating, destination?.rating.toString()),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.width(8.dp))
                            
                            // Stars
                            repeat(5) { index ->
                                val tint = if (index < (destination?.rating ?: 0f)) {
                                    Color(0xFFFFC107) // Amber color for filled stars
                                } else {
                                    Color.LightGray
                                }
                                
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = tint,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Description Section
                        Text(
                            text = stringResource(R.string.about),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = destination?.description ?: "",
                            fontSize = 16.sp,
                            lineHeight = 24.sp
                        )
                        
                        Spacer(modifier = Modifier.height(24.dp))
                        
                        // Points of Interest Section
                        Text(
                            text = stringResource(R.string.points_of_interest),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        destination?.pointsOfInterest?.forEach { point ->
                            PointOfInterestItem(point)
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Reviews Section
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.reviews),
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold
                            )
                            
                            Button(
                                onClick = { showReviewDialog = true },
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                            ) {
                                Text(stringResource(R.string.add_review))
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Reviews content
                        if (isLoadingReviews && reviews.isEmpty()) {
                            LoadingIndicator()
                        } else if (reviewsError != null) {
                            ErrorMessage(
                                message = reviewsError ?: stringResource(R.string.unknown_error),
                                onRetry = { viewModel.loadReviews(destinationId) }
                            )
                        } else if (reviews.isEmpty()) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = stringResource(R.string.no_reviews_yet),
                                    fontSize = 16.sp,
                                    color = Color.Gray,
                                    textAlign = TextAlign.Center
                                )
                            }
                        } else {
                            reviews.forEach { review ->
                                ReviewItem(review = review)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
            
            // Review Dialog
            if (showReviewDialog) {
                AlertDialog(
                    onDismissRequest = { showReviewDialog = false },
                    title = { Text(stringResource(R.string.write_review)) },
                    text = {
                        Column {
                            if (reviewSubmissionError != null) {
                                Text(
                                    text = reviewSubmissionError ?: "",
                                    color = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.padding(bottom = 16.dp)
                                )
                            }
                            
                            Text(
                                text = stringResource(R.string.your_rating),
                                fontWeight = FontWeight.Medium
                            )
                            
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            // Star rating selector
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                for (i in 1..5) {
                                    IconButton(
                                        onClick = { reviewRating = i },
                                        modifier = Modifier.size(42.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = "Rating $i",
                                            tint = if (i <= reviewRating) Color(0xFFFFC107) else Color.LightGray,
                                            modifier = Modifier.size(32.dp)
                                        )
                                    }
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = reviewerName,
                                onValueChange = { reviewerName = it },
                                label = { Text(stringResource(R.string.your_name)) },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true
                            )
                            
                            Spacer(modifier = Modifier.height(16.dp))
                            
                            OutlinedTextField(
                                value = reviewText,
                                onValueChange = { reviewText = it },
                                label = { Text(stringResource(R.string.your_review)) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp),
                                maxLines = 5
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (reviewerName.isNotBlank() && reviewText.isNotBlank()) {
                                    viewModel.submitReview(
                                        destinationId = destinationId,
                                        rating = reviewRating.toFloat(),
                                        reviewerName = reviewerName,
                                        comment = reviewText
                                    )
                                }
                            },
                            enabled = !isSubmittingReview && reviewerName.isNotBlank() && reviewText.isNotBlank()
                        ) {
                            if (isSubmittingReview) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(stringResource(R.string.submit_review))
                            }
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showReviewDialog = false }) {
                            Text(stringResource(R.string.back))
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PointOfInterestItem(point: String) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .padding(top = 8.dp)
                .background(MaterialTheme.colorScheme.primary, shape = CircleShape)
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        Text(
            text = point,
            fontSize = 16.sp
        )
    }
}

@Composable
fun ReviewItem(review: Review) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = review.reviewerName,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Rating stars
                repeat(5) { index ->
                    val tint = if (index < review.rating) {
                        Color(0xFFFFC107) // Amber color for filled stars
                    } else {
                        Color.LightGray
                    }
                    
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = tint,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = review.comment,
                fontSize = 14.sp
            )
        }
    }
} 