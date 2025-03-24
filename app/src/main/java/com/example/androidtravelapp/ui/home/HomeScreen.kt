package com.example.androidtravelapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.androidtravelapp.R
import com.example.androidtravelapp.data.model.Destination
import com.example.androidtravelapp.ui.common.DestinationCard
import com.example.androidtravelapp.ui.common.ErrorMessage
import com.example.androidtravelapp.ui.common.LoadingIndicator
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onDestinationClick: (Int) -> Unit,
    onLanguageChanged: (Locale) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val destinations by viewModel.destinations.collectAsStateWithLifecycle()
    val popularDestinations by viewModel.popularDestinations.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val activeSearchTerm by viewModel.activeSearchTerm.collectAsStateWithLifecycle()
    
    var showLanguageMenu by remember { mutableStateOf(false) }
    val lazyListState = rememberLazyListState()
    
    // Handle loading more data when scrolling near the end
    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.layoutInfo.visibleItemsInfo }
            .collect { visibleItems ->
                if (visibleItems.isNotEmpty()) {
                    val lastVisibleItem = visibleItems.last()
                    val totalItemsCount = lazyListState.layoutInfo.totalItemsCount
                    if (
                        lastVisibleItem.index >= totalItemsCount - 3 &&
                        !isLoading &&
                        destinations.isNotEmpty()
                    ) {
                        viewModel.loadMoreDestinations()
                    }
                }
            }
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top bar with app title and language button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Title
            Text(
                text = stringResource(R.string.home_title),
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
            
            // Language dropdown
            Box {
                IconButton(onClick = { showLanguageMenu = !showLanguageMenu }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = stringResource(R.string.change_language)
                    )
                }
                
                DropdownMenu(
                    expanded = showLanguageMenu,
                    onDismissRequest = { showLanguageMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.language_english)) },
                        onClick = {
                            onLanguageChanged(Locale("en"))
                            showLanguageMenu = false
                        }
                    )
                    DropdownMenuItem(
                        text = { Text(stringResource(R.string.language_french)) },
                        onClick = {
                            onLanguageChanged(Locale("fr"))
                            showLanguageMenu = false
                        }
                    )
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
                modifier = Modifier
                    .weight(1f),
                placeholder = { Text(stringResource(R.string.search_hint)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                keyboardActions = KeyboardActions(
                    onSearch = { viewModel.executeSearch() }
                )
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Button(
                onClick = { viewModel.executeSearch() },
                modifier = Modifier.height(56.dp)
            ) {
                Text(stringResource(R.string.search_button))
            }
        }
        
        // Error message
        if (error != null) {
            ErrorMessage(
                message = error!!,
                onRetry = { viewModel.refreshDestinations() }
            )
        } else if (isLoading && destinations.isEmpty()) {
            // Show loading indicator when initially loading
            LoadingIndicator()
        } else {
            // Content
            LazyColumn(
                state = lazyListState,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (popularDestinations.isNotEmpty() && activeSearchTerm.isEmpty()) {
                    item {
                        PopularDestinationsSection(
                            popularDestinations = popularDestinations,
                            onDestinationClick = onDestinationClick
                        )
                    }
                }
                
                item {
                    // Featured Destinations
                    Text(
                        text = if (activeSearchTerm.isEmpty()) 
                            stringResource(R.string.popular_destinations)
                        else 
                            stringResource(R.string.search_results),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(bottom = 8.dp, top = 8.dp)
                    )
                }
                
                if (destinations.isEmpty() && !isLoading) {
                    // Show message when no destinations match search
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_destinations_found),
                                fontSize = 16.sp,
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    // Show destinations
                    items(destinations) { destination ->
                        DestinationCard(
                            destination = destination,
                            onClick = { onDestinationClick(destination.id) },
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                    }
                    
                    // Show loading indicator when loading more items
                    if (isLoading) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PopularDestinationsSection(
    popularDestinations: List<Destination>,
    onDestinationClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = stringResource(R.string.featured_destinations),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        
        // TODO: Implement a horizontal scrolling list of popular destinations
        // For now, just show the first popular destination
        if (popularDestinations.isNotEmpty()) {
            val destination = popularDestinations.first()
            DestinationCard(
                destination = destination,
                onClick = { onDestinationClick(destination.id) },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
} 