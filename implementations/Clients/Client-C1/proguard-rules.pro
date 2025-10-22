# Preserve Kotlin classes and methods
-keep class kotlin.** { *; }
-keep class kotlinx.** { *; }
-dontwarn kotlin.**
-dontwarn kotlinx.**

# Preserve Hilt
-keep class dagger.** { *; }
-keep class com.google.dagger.** { *; }
-dontwarn dagger.**
-dontwarn com.google.dagger.**

# Preserve Ktor
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**

# Preserve Gson
-keep class com.google.gson.** { *; }
-dontwarn com.google.gson.**

# Preserve Retrofit
-keep class retrofit2.** { *; }
-dontwarn retrofit2.**

# Preserve AndroidX
-keep class androidx.** { *; }
-dontwarn androidx.**

# Keep data classes
-keep class org.pcfx.client.c1.data.** { *; }
-keep class org.pcfx.client.c1.ui.viewmodel.** { *; }

# Keep Composable functions
-keep @androidx.compose.runtime.Composable class * { *; }
