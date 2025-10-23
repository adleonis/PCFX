package org.pcfx.client.c1.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.pcfx.client.c1.ui.viewmodel.ActivityItem

@Composable
fun ActivityCard(item: ActivityItem) {
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { isExpanded.value = !isExpanded.value },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = item.subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = item.ts,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Icon(
                    imageVector = if (isExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isExpanded.value) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Type: ${item.type.uppercase()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                    ExpandedDetailsContent(details = item.details)
                }
            }
        }
    }
}

@Composable
fun EventCard(event: Map<String, Any>) {
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { isExpanded.value = !isExpanded.value },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val eventData = event["event"] as? Map<String, Any>
            val source = eventData?.get("source") as? Map<String, Any>
            val content = eventData?.get("content") as? Map<String, Any>

            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${source?.get("app") ?: "Unknown App"}",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Surface: ${source?.get("surface") ?: "unknown"} | Content: ${content?.get("kind") ?: "unknown"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = event["ts"]?.toString() ?: "Unknown time",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Icon(
                    imageVector = if (isExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isExpanded.value) {
                ExpandedEventDetails(event = event, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}

@Composable
fun AtomCard(atom: Map<String, Any>) {
    val isExpanded = remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable { isExpanded.value = !isExpanded.value },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            val atomData = atom["atom"] as? Map<String, Any>

            androidx.compose.foundation.layout.Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = atomData?.get("text")?.toString() ?: "Knowledge Insight",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 2
                    )
                    Text(
                        text = "From: ${atom["node_id"]?.toString() ?: "Unknown node"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = atom["ts"]?.toString() ?: "Unknown time",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
                Icon(
                    imageVector = if (isExpanded.value) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            if (isExpanded.value) {
                ExpandedDetailsContent(details = atomData ?: atom, modifier = Modifier.padding(top = 12.dp))
            }
        }
    }
}

@Composable
fun ExpandedEventDetails(event: Map<String, Any>, modifier: Modifier = Modifier) {
    val eventData = event["event"] as? Map<String, Any>
    val source = eventData?.get("source") as? Map<String, Any>
    val content = eventData?.get("content") as? Map<String, Any>
    val privacy = eventData?.get("privacy") as? Map<String, Any>

    Column(modifier = modifier) {
        DetailsRow(label = "Event ID", value = event["id"]?.toString() ?: "N/A")
        DetailsRow(label = "Device", value = event["device"]?.toString() ?: "Unknown")
        DetailsRow(label = "Adapter ID", value = eventData?.get("adapter_id")?.toString() ?: "N/A")

        Text("Source", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
        DetailsRow(label = "  App", value = source?.get("app")?.toString() ?: "Unknown", indent = true)
        DetailsRow(label = "  Surface", value = source?.get("surface")?.toString() ?: "Unknown", indent = true)
        DetailsRow(label = "  Frame", value = source?.get("frame")?.toString() ?: "Unknown", indent = true)

        Text("Content", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
        DetailsRow(label = "  Kind", value = content?.get("kind")?.toString() ?: "Unknown", indent = true)
        DetailsRow(label = "  Text", value = content?.get("text")?.toString() ?: "N/A", indent = true)
        DetailsRow(label = "  Language", value = content?.get("lang")?.toString() ?: "Unknown", indent = true)

        if (privacy != null) {
            Text("Privacy", style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(top = 8.dp))
            DetailsRow(label = "  Consent ID", value = privacy["consent_id"]?.toString() ?: "N/A", indent = true)
            DetailsRow(label = "  Retention Days", value = privacy["retention_days"]?.toString() ?: "N/A", indent = true)
        }
    }
}

@Composable
fun ExpandedDetailsContent(details: Map<String, Any>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        details.forEach { (key, value) ->
            if (value != null && value !is Map<*, *> && value !is List<*>) {
                DetailsRow(label = key, value = value.toString())
            }
        }
    }
}

@Composable
fun DetailsRow(label: String, value: String, indent: Boolean = false) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = if (indent) Modifier.padding(start = 8.dp) else Modifier
        )
    }
}
