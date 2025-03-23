package com.example.androidtravelapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androidtravelapp.ui.theme.AndroidTravelAppTheme
import com.example.androidtravelapp.R

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidTravelAppTheme {
                TravelAppNavigation()
            }
        }
    }
}

@Composable
fun TravelAppNavigation() {
    var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }
    
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        when (val screen = currentScreen) {
            is Screen.Home -> {
                TravelHomeScreen(
                    onDestinationClick = { destinationId ->
                        currentScreen = Screen.DestinationDetail(destinationId)
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
            is Screen.DestinationDetail -> {
                DestinationDetailScreen(
                    destinationId = screen.destinationId,
                    onBackPressed = {
                        currentScreen = Screen.Home
                    },
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    data class DestinationDetail(val destinationId: Int) : Screen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelHomeScreen(
    onDestinationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // App Title
        Text(
            text = stringResource(R.string.home_title),
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            placeholder = { Text(stringResource(R.string.search_hint)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            singleLine = true
        )
        
        // Featured Destinations
        Text(
            text = stringResource(R.string.popular_destinations),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
        ) {
            items(sampleDestinations) { destination ->
                DestinationCard(
                    destination = destination,
                    onClick = { onDestinationClick(destination.id) },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DestinationCard(
    destination: Destination,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 4.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = destination.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = destination.location,
                fontSize = 14.sp,
                color = Color.Gray
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = destination.description,
                fontSize = 14.sp,
                maxLines = 2
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Rating display
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.rating, destination.rating.toString()),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
                
                Spacer(modifier = Modifier.width(4.dp))
                
                // Simple rating stars
                repeat(5) { index ->
                    val tint = if (index < destination.rating) {
                        Color(0xFFFFC107) // Amber color for filled stars
                    } else {
                        Color.LightGray // Light gray for empty stars
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
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onClick,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text(stringResource(R.string.view_details))
                }
            }
        }
    }
}

// Data class for destinations
data class Destination(
    val id: Int,
    val name: String,
    val description: String,
    val location: String,
    val rating: Float,
    val pointsOfInterest: List<String> = emptyList()
)

// Sample data
val sampleDestinations = listOf(
    Destination(
        id = 1,
        name = "Paris",
        description = "The City of Light, known for the Eiffel Tower, Louvre Museum, and charming cafes. Paris has been one of the world's most popular tourist destinations for decades, known for its iconic landmarks, world-class museums, and exquisite cuisine. The city's romantic atmosphere, historic architecture, and artistic heritage make it a must-visit location for travelers from all over the world.",
        location = "France",
        rating = 4.8f,
        pointsOfInterest = listOf(
            "Eiffel Tower",
            "Louvre Museum",
            "Notre-Dame Cathedral",
            "Champs-Élysées",
            "Arc de Triomphe"
        )
    ),
    Destination(
        id = 2,
        name = "Tokyo",
        description = "A bustling metropolis that offers a unique blend of traditional and modern experiences. Tokyo is a city of contrasts, where ancient temples stand in the shadows of ultra-modern skyscrapers. Visitors can explore traditional gardens, visit historic shrines, shop in futuristic malls, and dine at some of the world's finest restaurants all in one day.",
        location = "Japan",
        rating = 4.7f,
        pointsOfInterest = listOf(
            "Tokyo Skytree",
            "Senso-ji Temple",
            "Shibuya Crossing",
            "Meiji Shrine",
            "Akihabara Electric Town"
        )
    ),
    Destination(
        id = 3,
        name = "New York City",
        description = "The Big Apple features iconic landmarks like Times Square, Central Park, and the Statue of Liberty. New York City is a global center for media, culture, fashion, and finance. The city that never sleeps offers visitors endless entertainment options, world-class museums, diverse cuisine from around the world, and some of the most recognizable attractions on the planet.",
        location = "United States",
        rating = 4.6f,
        pointsOfInterest = listOf(
            "Statue of Liberty",
            "Central Park",
            "Empire State Building",
            "Times Square",
            "Metropolitan Museum of Art"
        )
    )
)

@Preview(showBackground = true)
@Composable
fun TravelHomeScreenPreview() {
    AndroidTravelAppTheme {
        TravelHomeScreen(onDestinationClick = {})
    }
}