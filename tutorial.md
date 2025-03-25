# Travel Guide Android App - Beginner's Guide

This tutorial will walk you through the Travel Guide Android application, explaining its architecture, key components, and how different parts work together. By the end, you'll have a solid understanding of modern Android development practices.

## Table of Contents

1. [Introduction](#introduction)
2. [Project Structure](#project-structure)
3. [Architecture Overview](#architecture-overview)
4. [Key Components](#key-components)
   - [UI with Jetpack Compose](#ui-with-jetpack-compose)
   - [Data Layer](#data-layer)
   - [Network Communication](#network-communication)
   - [Authentication](#authentication)
5. [Feature Walkthrough](#feature-walkthrough)
6. [Extending the App](#extending-the-app)

## Introduction

Travel Guide is a modern Android application built using Kotlin and following current best practices. The app allows users to:

- Browse popular travel destinations
- Search for destinations
- View detailed information about places
- Read and submit reviews

The app connects to a RESTful API backend deployed on Azure, which provides all the necessary data.

## Project Structure

The project follows a standard Android structure with some modern modifications:

```
app/
├── build.gradle.kts            # App-level build configuration
├── src/
    ├── main/
        ├── java/               # Kotlin source files
        │   └── com/example/androidtravelapp/
        │       ├── data/       # Data handling
        │       ├── di/         # Dependency injection
        │       ├── ui/         # User interface
        │       └── util/       # Utilities
        ├── res/                # Resources (layouts, strings, etc.)
        └── AndroidManifest.xml # App declaration
```

## Architecture Overview

The app follows the **MVVM (Model-View-ViewModel)** architecture pattern, with a clean separation of concerns:

1. **Model**: Data classes and repositories in the `data` package
2. **View**: Jetpack Compose UI components in the `ui` package
3. **ViewModel**: ViewModels that connect the UI with data in the `ui` package

Additionally, the app uses **Repository Pattern** to abstract data sources and **Dependency Injection** with Hilt to manage dependencies.

## Key Components

### UI with Jetpack Compose

The app uses Jetpack Compose, Android's modern declarative UI toolkit. Let's look at a simplified example from `HomeScreen.kt`:

```kotlin
@Composable
fun HomeScreen(
    onDestinationClick: (Int) -> Unit,
    onLanguageChanged: (Locale) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val destinations by viewModel.destinations.collectAsStateWithLifecycle()
    // ...
    
    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        // Search Bar
        Row(/*...*/) {
            OutlinedTextField(/*...*/)
            Button(onClick = { viewModel.executeSearch() }) {
                Text("Search")
            }
        }
        
        LazyColumn(/*...*/) {
            // Display destinations
            items(destinations) { destination ->
                DestinationCard(
                    destination = destination,
                    onClick = { onDestinationClick(destination.id) }
                )
            }
        }
    }
}
```

Key points:
- **Composable functions** define UI components
- **State hoisting** with ViewModels
- **Reactive UI** updates through StateFlow

### Data Layer

The data layer consists of:

1. **Models**: Simple data classes representing entities like `Destination.kt`:

```kotlin
data class Destination(
    val id: Int,
    val name: String,
    val description: String,
    // Other properties...
)
```

2. **Repositories**: Classes that abstract data operations like `DestinationRepository.kt`:

```kotlin
class DestinationRepository @Inject constructor(
    private val destinationService: DestinationService
) {
    suspend fun getDestinations(page: Int = 1): NetworkResult<List<Destination>> {
        return try {
            val response = destinationService.getAllDestinations()
            // Processing response...
        } catch (e: Exception) {
            NetworkResult.Error("Network error: ${e.message}")
        }
    }
    // Other methods...
}
```

### Network Communication

API communication is handled through Retrofit:

1. **ApiModule.kt**: Creates API services with Retrofit and OkHttp:

```kotlin
class ApiModule @Inject constructor() {
    companion object {
        private const val BASE_URL = "https://travelguidesimply-ewbvb6h4cyb8emhg.westeurope-01.azurewebsites.net/"
        
        // Create Retrofit instance...
        private fun createRetrofit(okHttpClient: OkHttpClient): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
        
        // Create API services...
        fun createDestinationService(tokenProvider: () -> String?): DestinationService {
            val okHttpClient = createOkHttpClient(tokenProvider)
            val retrofit = createRetrofit(okHttpClient)
            return retrofit.create(DestinationService::class.java)
        }
    }
}
```

2. **Service Interfaces**: Define API endpoints like `DestinationService.kt`:

```kotlin
interface DestinationService {
    @GET("api/destinations")
    suspend fun getAllDestinations(
        @Query("pageNumber") pageNumber: Int? = null,
        // Other parameters...
    ): ApiResponse<PaginatedResponse<Destination>>
    
    // Other API methods...
}
```

### Authentication

Authentication uses JWT tokens, handled by:

1. **TokenManager.kt**: Securely stores authentication tokens:

```kotlin
class TokenManager @Inject constructor(context: Context) {
    // Encrypted storage setup...
    
    fun saveAuthData(
        accessToken: String,
        refreshToken: String,
        expiration: Long,
        userData: UserData
    ) {
        prefs.edit()
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .putString(KEY_REFRESH_TOKEN, refreshToken)
            // Other data...
            .apply()
    }
    
    // Other token management methods...
}
```

2. **AuthRepository.kt**: Handles login, registration, and token refreshing:

```kotlin
class AuthRepository @Inject constructor(
    private val tokenManager: TokenManager,
    private val authService: AuthService
) {
    suspend fun login(username: String, password: String): NetworkResult<AuthResponse> {
        // Login implementation...
    }
    
    // Other authentication methods...
}
```

Note: Currently, login is not required to use most app features, as they use public API endpoints.

## Feature Walkthrough

### Home Screen and Destination Browsing

1. The app starts at `MainActivity.kt`, which hosts navigation
2. The initial destination is `HomeScreen.kt`
3. `HomeViewModel.kt` loads destinations from `DestinationRepository`
4. Destinations are displayed as cards using `DestinationCard` composable
5. Scrolling to the bottom triggers pagination to load more destinations

### Search Functionality

1. The search bar in `HomeScreen.kt` updates the search query in `HomeViewModel`
2. When the search button is clicked, `executeSearch()` is called
3. This triggers `DestinationRepository.searchDestinations()`
4. Results are displayed in the same list, replacing the current destinations

### Destination Details

1. Clicking a destination card navigates to `DestinationDetailScreen.kt`
2. This screen shows detailed information including description, points of interest, and reviews
3. Data is loaded by `DestinationDetailsViewModel` through repositories

## Extending the App

To extend the app, you might want to:

1. **Add user authentication UI**: Implement login/register screens and require authentication for certain features
2. **Add more filters**: Extend the search functionality with filters by region, rating, etc.
3. **Implement offline support**: Add a local database with Room to cache data
4. **Add personalization**: Allow users to save favorite destinations

## Key Technologies Used

- **Kotlin**: The primary programming language
- **Jetpack Compose**: Modern UI toolkit
- **Coroutines & Flow**: For asynchronous operations
- **Hilt**: For dependency injection
- **Retrofit & OkHttp**: For API communication
- **MVVM Architecture**: For clean separation of concerns
- **Repository Pattern**: To abstract data sources

This app serves as an excellent example of modern Android development practices. By studying and extending it, you'll become familiar with the tools and patterns used in professional Android development. 