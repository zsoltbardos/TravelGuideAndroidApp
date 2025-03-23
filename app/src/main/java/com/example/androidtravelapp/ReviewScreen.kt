package com.example.androidtravelapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtravelapp.ui.theme.AndroidTravelAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewScreen(
    destinationId: Int,
    onBackPressed: () -> Unit,
    onReviewSubmitted: () -> Unit,
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
    
    var reviewerName by remember { mutableStateOf("") }
    var reviewText by remember { mutableStateOf("") }
    var rating by remember { mutableStateOf(5) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.write_review)) },
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
                .padding(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            // Destination info
            Text(
                text = destination.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = destination.location,
                fontSize = 16.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )
            
            // Rating selector
            Text(
                text = stringResource(R.string.your_rating),
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Stars for rating
            Row(
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                repeat(5) { index ->
                    IconButton(onClick = { rating = index + 1 }) {
                        Icon(
                            imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.Star,
                            contentDescription = null,
                            tint = if (index < rating) Color(0xFFFFC107) else Color.LightGray,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }
            
            // Name input
            OutlinedTextField(
                value = reviewerName,
                onValueChange = { reviewerName = it },
                label = { Text(stringResource(R.string.your_name)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words
                ),
                singleLine = true
            )
            
            // Review text input
            OutlinedTextField(
                value = reviewText,
                onValueChange = { reviewText = it },
                label = { Text(stringResource(R.string.your_review)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences
                )
            )
            
            // Submit button
            Button(
                onClick = {
                    // In a real app, would send to backend
                    // For now, just navigate back
                    onReviewSubmitted()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                enabled = reviewerName.isNotEmpty() && reviewText.isNotEmpty()
            ) {
                Text(stringResource(R.string.submit_review))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewScreenPreview() {
    AndroidTravelAppTheme {
        ReviewScreen(
            destinationId = 1,
            onBackPressed = {},
            onReviewSubmitted = {}
        )
    }
} 