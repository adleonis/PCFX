# PDV Client-C1

A modern Android client application for visualizing and analyzing data from the Phenomenon Data View (PDV) system.

## Overview

Client-C1 is a standalone Android native application that connects to a PDV server to display:
- **Activity Feed**: Real-time timeline of exposure events and knowledge atoms
- **Events Panel**: Filtered view of exposure events with basic filtering
- **Insights Panel**: Extracted entities, sentiment analysis, and confidence scores
- **Statistics Dashboard**: Aggregated metrics and distribution charts
- **Settings & Search**: PDV server configuration and full-text search

## Architecture

### Technology Stack
- **UI Framework**: Jetpack Compose (Modern declarative UI)
- **Architecture**: MVVM with StateFlow for reactive state management
- **Networking**: Ktor Client for HTTP communication
- **Dependency Injection**: Hilt Android for DI
- **Data Storage**: DataStore for persistent preferences
- **Logging**: SLF4J with Logback backend
- **Serialization**: Kotlinx Serialization and Gson

### Project Structure

```
Client-C1/
├── src/main/
│   ├── kotlin/org/pcfx/client/c1/
│   │   ├── ClientC1App.kt                    # Hilt entry point
│   │   ├── config/
│   │   │   └── BuildConfig.kt                # Build-time configuration
│   │   ├── data/
│   │   │   ├── models/                       # Data classes
│   │   │   │   ├── ExposureEvent.kt
│   │   │   │   └── KnowledgeAtom.kt
│   │   │   ├── network/
│   │   │   │   └── PDVClient.kt              # Ktor-based API client
│   │   │   └── preferences/
│   │   │       └── SettingsPreferences.kt    # DataStore configuration
│   │   ├── di/
│   │   │   └── AppModule.kt                  # Hilt DI module
│   │   └── ui/
│   │       ├── MainActivity.kt                # Entry activity
│   │       ├── screens/                      # 5 full-screen Compose screens
│   │       │   ├── ActivityFeedScreen.kt
│   │       │   ├── EventsScreen.kt
│   │       │   ├── InsightsScreen.kt
│   │       │   ├── StatisticsScreen.kt
│   │       │   └── SettingsScreen.kt
│   │       ├── viewmodel/                    # MVVM ViewModels
│   │       │   ├── ActivityFeedViewModel.kt
│   │       │   ├── EventsViewModel.kt
│   │       │   ├── InsightsViewModel.kt
│   │       │   ├── StatisticsViewModel.kt
│   │       │   └── SettingsViewModel.kt
│   │       ├── components/
│   │       │   └── Cards.kt                  # Reusable UI components
│   │       └── theme/
│   │           ├── Theme.kt                  # Dark grey & blue theme
│   │           └── Typography.kt             # Text styles
│   └── res/
│       └── values/
│           ├── strings.xml
│           └── themes.xml
├── build.gradle.kts                          # App module gradle config
├── AndroidManifest.xml
├── settings.gradle.kts
├── gradlew / gradlew.bat                     # Gradle wrappers
└── proguard-rules.pro
```

## Building & Running

### Prerequisites
- Android Studio Arctic Fox or later
- Java 11+
- Android SDK 34

### Build

```bash
cd implementations/Clients/Client-C1

# On macOS/Linux
./gradlew build

# On Windows
gradlew.bat build
```

### Run

```bash
./gradlew installDebug

# Then launch from Android Studio or via command line:
adb shell am start -n org.pcfx.client.c1/.ui.MainActivity
```

## Configuration

The app connects to a PDV server. Configure the connection in **Settings**:

- **Default**: `localhost:7777`
- **Configurable**: Enter custom host and port via the Settings tab
- **Auto-refresh**: Enable/disable auto-refresh (default: enabled every 5 seconds)

## Features

### 1. Activity Feed
- Chronological timeline combining events and atoms
- Auto-refreshes every 5 seconds (configurable)
- Shows device, source, and content type

### 2. Events Panel
- Filter by content kind (text, audio, image, video, ad, system)
- Filter by surface (app, browser, audio, tv, wearable, system)
- Displays up to 50 most recent events
- Combinable filters

### 3. Insights Panel
- Top extracted entities with mention counts
- Sentiment distribution (positive, negative, neutral)
- Average confidence score for atom analysis
- Sortable by frequency

### 4. Statistics Dashboard
- Total events, atoms, and metrics
- Distribution breakdown by surface
- Distribution breakdown by content kind
- Real-time metrics

### 5. Settings & Search
- Configure PDV host and port
- Toggle auto-refresh and set interval
- Full-text search across atom content
- Display app version and unique app ID

## Data Models

### ExposureEvent
Raw data capture from adapters containing:
- Device identifier
- Source surface (app, browser, etc.)
- Content kind and text
- Privacy/consent information
- Capabilities used

### KnowledgeAtom
Analyzed insights extracted by Nodes:
- Original event provenance
- Extracted text/knowledge
- Named entities with types
- Sentiment and tone analysis
- Confidence scores
- Vector embedding references

## Theming

The app uses a **minimal, modern dark theme**:
- **Primary**: Bright blue (`#1E90FF`)
- **Background**: Dark grey (`#1F1F1F`)
- **Surface**: Darker grey (`#2A2A2A`)
- **Text**: Very light grey (`#E8E8E8`)
- **Accent**: Blue for highlights

Easily customizable in `ui/theme/Theme.kt`.

## Networking

PDV Client uses **Ktor Client** for HTTP communication:

```kotlin
// Fetch events
val events = pdvClient.getEvents(since = "2025-10-22T00:00:00Z", limit = 50)

// Fetch atoms
val atoms = pdvClient.getAtoms(since = "2025-10-22T00:00:00Z", limit = 50)

// Fetch statistics
val stats = pdvClient.getStats()
```

All API calls are non-blocking coroutines.

## MVVM State Management

Each screen has a dedicated ViewModel:
- **State**: Immutable `StateFlow<ScreenState>`
- **Actions**: Public suspend functions
- **Lifecycle**: Auto-managed with Hilt `@HiltViewModel`

Example:
```kotlin
@HiltViewModel
class EventsViewModel @Inject constructor(...) : ViewModel() {
    private val _state = MutableStateFlow(EventsState())
    val state: StateFlow<EventsState> = _state.asStateFlow()
    
    fun loadEvents() { ... }
}
```

## Dependencies

- `androidx.compose.ui:ui:1.7.6`
- `io.ktor:ktor-client-android:2.3.5`
- `com.google.dagger:hilt-android:2.48`
- `androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7`
- `androidx.datastore:datastore-preferences:1.1.1`

## Future Enhancements

- Entity relationship graph visualization
- Custom date/time filtering
- Export data to CSV/JSON
- Advanced search with entity type filters
- Offline caching with Room database
- Real-time WebSocket updates
- Dark/light theme toggle

## Support

For issues or questions about the PDV system, refer to the main project documentation at `/spec` and `/docs`.
