package com.example.androidtravelapp

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtravelapp.ui.theme.AndroidTravelAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationDetailScreen(
    destinationId: Int,
    onBackPressed: () -> Unit,
    onAddReviewClick: (Int) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Find the destination by ID
    val destination = sampleDestinations.find { it.id == destinationId }
    
    if (destination == null) {
        // Show error if destination not found
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.destination_not_found),
                style = MaterialTheme.typography.headlineMedium
            )
        }
        return
    }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(destination.name) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(Icons.Default.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Destination Title and Location
            Text(
                text = destination.name,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = destination.location,
                fontSize = 18.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Rating Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = stringResource(R.string.rating, destination.rating.toString()),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Stars
                repeat(5) { index ->
                    val tint = if (index < destination.rating) {
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
                text = destination.description,
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
            
            destination.pointsOfInterest.forEach { point ->
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
                    onClick = { onAddReviewClick(destinationId) },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Text(stringResource(R.string.add_review))
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Sample reviews
            ReviewItem(
                name = "John Doe",
                rating = 5.0f,
                comment = stringResource(R.string.sample_review_1)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            ReviewItem(
                name = "Jane Smith",
                rating = 4.5f,
                comment = stringResource(R.string.sample_review_2)
            )
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
fun ReviewItem(name: String, rating: Float, comment: String) {
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
                    text = name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Rating stars
                repeat(5) { index ->
                    val tint = if (index < rating) {
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
                text = comment,
                fontSize = 14.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DestinationDetailScreenPreview() {
    AndroidTravelAppTheme {
        DestinationDetailScreen(
            destinationId = 1,
            onBackPressed = {},
            onAddReviewClick = {}
        )
    }
} 