# PCF-X Adapter ProGuard Rules

# Keep the main application class
-keep class org.pcfx.adapter.android.** { *; }

# Keep data models and serialization
-keep class org.pcfx.adapter.android.model.** { *; }
-keep class org.pcfx.adapter.android.db.** { *; }

# Keep Gson
-keep class com.google.gson.** { *; }
-keep class * implements com.google.gson.JsonDeserializer
-keep class * implements com.google.gson.JsonSerializer

# Keep JSON Schema validator
-keep class org.everit.json.schema.** { *; }
-keep class org.json.** { *; }

# Keep Bouncy Castle
-keep class org.bouncycastle.** { *; }

# Keep Room database
-keep class androidx.room.** { *; }
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class * { *; }
-keep @androidx.room.Dao interface * { *; }

# Keep OkHttp
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }

# Keep Android services
-keep class org.pcfx.adapter.android.service.** extends android.app.Service { *; }
-keep class org.pcfx.adapter.android.service.** extends android.accessibilityservice.AccessibilityService { *; }
-keep class org.pcfx.adapter.android.service.** extends android.service.notification.NotificationListenerService { *; }

# Keep Activities
-keep class org.pcfx.adapter.android.ui.** extends android.app.Activity { *; }

# Keep Kotlin metadata
-keepclassmembers class ** {
    *** Companion;
}

# Keep Kotlin coroutines
-keep class kotlin.coroutines.** { *; }
-keepclassmembers class * implements kotlin.coroutines.Continuation {
    *** baseContinuationImpl(...);
}

# Keep BuildConfig
-keep class org.pcfx.adapter.android.BuildConfig { *; }

# Suppress warnings
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.bouncycastle.**
