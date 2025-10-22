package org.pcfx.client.c1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import org.pcfx.client.c1.ui.viewmodel.EventsViewModel
import org.pcfx.client.c1.ui.components.EventCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EventsScreen(
    viewModel: EventsViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val filteredEvents = viewModel.getFilteredEvents()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Events",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )

        if (state.value.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            Column(modifier = Modifier.padding(top = 16.dp)) {
                Text(
                    text = "Filter by Content Type",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    state.value.contentKindOptions.forEach { kind ->
                        AssistChip(
                            onClick = { viewModel.filterByContentKind(if (state.value.selectedContentKind == kind) null else kind) },
                            label = { Text(kind) },
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                        )
                    }
                }

                Text(
                    text = "Filter by Surface",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                FlowRow(
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    state.value.surfaceOptions.forEach { surface ->
                        AssistChip(
                            onClick = { viewModel.filterBySurface(if (state.value.selectedSurface == surface) null else surface) },
                            label = { Text(surface) },
                            modifier = Modifier.padding(end = 8.dp, bottom = 8.dp)
                        )
                    }
                }

                Text(
                    text = "Results (${filteredEvents.size})",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                LazyColumn {
                    items(filteredEvents) { event ->
                        EventCard(event = event)
                    }
                }
            }
        }
    }
}
