package org.pcfx.client.c1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.pcfx.client.c1.ui.viewmodel.ActivityFeedViewModel
import org.pcfx.client.c1.ui.viewmodel.FeedType
import org.pcfx.client.c1.ui.components.ActivityCard
import org.pcfx.client.c1.ui.components.AtomCard
import org.pcfx.client.c1.ui.components.EventCard

@Composable
fun ActivityFeedScreen(
    viewModel: ActivityFeedViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val lazyListState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleIndex = lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val totalItemsCount = lazyListState.layoutInfo.totalItemsCount
            lastVisibleIndex >= totalItemsCount - 3
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && !state.value.isLoadingMore && state.value.hasMoreItems) {
            viewModel.loadMoreActivity()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Tab Row for switching between Events and Atoms
        TabRow(
            selectedTabIndex = if (state.value.feedType == FeedType.EVENTS) 0 else 1,
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = state.value.feedType == FeedType.EVENTS,
                onClick = { viewModel.switchFeedType(FeedType.EVENTS) },
                text = { Text("Events") }
            )
            Tab(
                selected = state.value.feedType == FeedType.ATOMS,
                onClick = { viewModel.switchFeedType(FeedType.ATOMS) },
                text = { Text("Atoms") }
            )
        }

        // Content area with header and list
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Text(
                text = if (state.value.feedType == FeedType.EVENTS) "Events Feed" else "Atoms Feed",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            when {
                state.value.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                state.value.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Error: ${state.value.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                state.value.items.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No ${if (state.value.feedType == FeedType.EVENTS) "events" else "atoms"} yet",
                            color = MaterialTheme.colorScheme.onBackground,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        state = lazyListState,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 16.dp)
                    ) {
                        items(state.value.items) { item ->
                            when (item.type) {
                                "event" -> {
                                    val eventMap = mapOf(
                                        "id" to item.id,
                                        "ts" to item.ts,
                                        "device" to "",
                                        "event" to item.details
                                    )
                                    EventCard(event = eventMap)
                                }
                                "atom" -> {
                                    val atomMap = mapOf(
                                        "id" to item.id,
                                        "ts" to item.ts,
                                        "node_id" to "",
                                        "atom" to item.details
                                    )
                                    AtomCard(atom = atomMap)
                                }
                                else -> ActivityCard(item = item)
                            }
                        }

                        if (state.value.isLoadingMore) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
