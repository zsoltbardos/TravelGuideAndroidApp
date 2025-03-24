package com.example.androidtravelapp.data.repository

import com.example.androidtravelapp.data.api.DestinationService
import com.example.androidtravelapp.data.model.ApiResponse
import com.example.androidtravelapp.data.model.Destination
import com.example.androidtravelapp.data.model.DestinationRequest
import com.example.androidtravelapp.data.model.PaginatedResponse
import com.example.androidtravelapp.util.NetworkResult
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

// Helper class to return both destinations and pagination info
data class PaginatedDestinations(
    val destinations: List<Destination>,
    val hasNext: Boolean,
    val totalPages: Int,
    val currentPage: Int
)

@Singleton
class DestinationRepository @Inject constructor(
    private val destinationService: DestinationService
) {
    // Mock data
    private val mockDestinations = listOf(
        Destination(
            id = 1,
            name = "Paris",
            description = "Paris, France's capital, is a major European city and a global center for art, fashion, gastronomy and culture. Its 19th-century cityscape is crisscrossed by wide boulevards and the River Seine.",
            coverImageUrl = "https://images.unsplash.com/photo-1502602898657-3e91760cbb34?q=80&w=1000&auto=format&fit=crop",
            location = "France, Europe",
            rating = 4.7f,
            pointsOfInterest = listOf("Eiffel Tower", "Louvre Museum", "Notre-Dame Cathedral", "Arc de Triomphe", "Champs-Élysées"),
            region = "Europe",
            category = "City",
            reviewCount = 128
        ),
        Destination(
            id = 2,
            name = "Kyoto",
            description = "Kyoto, once the capital of Japan, is a city on the island of Honshu. It's famous for its numerous classical Buddhist temples, as well as gardens, imperial palaces, Shinto shrines and traditional wooden houses.",
            coverImageUrl = "https://www.gotokyo.org/en/destinations/western-tokyo/images/401_1485_1.jpg",
            location = "Japan, Asia",
            rating = 4.9f,
            pointsOfInterest = listOf("Fushimi Inari Shrine", "Kinkaku-ji (Golden Pavilion)", "Arashiyama Bamboo Grove", "Gion District", "Kiyomizu-dera Temple"),
            region = "Asia",
            category = "Historical",
            reviewCount = 94
        ),
        Destination(
            id = 3,
            name = "Santorini",
            description = "Santorini is one of the Cyclades islands in the Aegean Sea. It was devastated by a volcanic eruption in the 16th century BC, forever shaping its rugged landscape.",
            coverImageUrl = "https://www.travelandleisure.com/thmb/RpGq1vNT2RrUGtU-k0OqbzIKWF8=/1500x0/filters:no_upscale():max_bytes(150000):strip_icc()/santorini-greece-SANTORINIGUIDE0323-38b4d3962c064c869eb2882da98d7741.jpg",
            location = "Greece, Europe",
            rating = 4.8f,
            pointsOfInterest = listOf("Oia", "Fira", "Red Beach", "Ancient Thira", "Akrotiri Archaeological Site"),
            region = "Europe",
            category = "Island",
            reviewCount = 78
        ),
        Destination(
            id = 4,
            name = "Machu Picchu",
            description = "Machu Picchu is an Incan citadel set high in the Andes Mountains in Peru, above the Urubamba River valley. Built in the 15th century and later abandoned, it's renowned for its sophisticated dry-stone walls that fuse huge blocks without the use of mortar.",
            coverImageUrl = "https://www.nationalgeographic.com/content/dam/travel/2017-digital/family-travel/top-10-ruins/2_macchu-picchu-peru.jpg",
            location = "Peru, South America",
            rating = 4.9f,
            pointsOfInterest = listOf("Sun Gate", "Intihuatana Stone", "Temple of the Sun", "Huayna Picchu", "Main Plaza"),
            region = "South America",
            category = "Historical",
            reviewCount = 102
        ),
        Destination(
            id = 5,
            name = "Serengeti National Park",
            description = "The Serengeti National Park in Tanzania is a vast treeless plain with millions of animals living or passing through in search of fresh grasslands. It's most famous for the annual wildebeest migration.",
            coverImageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/b/bd/Serengeti_sunset_with_lions.jpg/1200px-Serengeti_sunset_with_lions.jpg",
            location = "Tanzania, Africa",
            rating = 4.8f,
            pointsOfInterest = listOf("Wildebeest Migration", "Seronera Valley", "Moru Kopjes", "Grumeti River", "Lobo Valley"),
            region = "Africa",
            category = "Nature",
            reviewCount = 84
        )
    )

    suspend fun getDestinations(page: Int = 1, pageSize: Int = 10): NetworkResult<List<Destination>> {
        return try {
            // We found out that adding query parameters causes 500 errors
            // So we'll use the endpoint without parameters
            val response = destinationService.getAllDestinations(
                pageNumber = null,
                pageSize = null,
                sortBy = null,
                sortOrder = null
            )
            
            if (response.success && response.data != null) {
                // Extract items from the paginated response
                NetworkResult.Success(response.data.items)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun getDestinationById(id: Int): NetworkResult<Destination> {
        return try {
            val response = destinationService.getDestinationById(id)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun searchDestinations(
        query: String,
        page: Int = 1,
        pageSize: Int = 10
    ): NetworkResult<List<Destination>> {
        return try {
            // Omit pagination parameters to avoid 500 error
            val response = destinationService.searchDestinations(
                query,
                pageNumber = null,
                pageSize = null
            )
            
            if (response.success && response.data != null) {
                // Extract items from the paginated response
                NetworkResult.Success(response.data.items)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun getPopularDestinations(count: Int = 5): NetworkResult<List<Destination>> {
        return try {
            val response = destinationService.getPopularDestinations(count)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun createDestination(destinationRequest: DestinationRequest): NetworkResult<Destination> {
        return try {
            val response = destinationService.createDestination(destinationRequest)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun updateDestination(id: Int, destinationRequest: DestinationRequest): NetworkResult<Destination> {
        return try {
            val response = destinationService.updateDestination(id, destinationRequest)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    
    suspend fun deleteDestination(id: Int): NetworkResult<Boolean> {
        return try {
            val response = destinationService.deleteDestination(id)
            
            if (response.success && response.data != null) {
                NetworkResult.Success(response.data)
            } else {
                NetworkResult.Error(response.message ?: "Unknown error occurred")
            }
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
} 