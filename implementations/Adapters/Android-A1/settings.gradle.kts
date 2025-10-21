// C:/CODE/PCFX/implementations/Adapters/Android-A1/settings.gradle.kts

// This block MUST be at the top. It tells Gradle where to find the plugins
// defined in the build.gradle.kts file.
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

// This block tells Gradle where to find the project's library dependencies.
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

// This sets the name of the root project for this build.
rootProject.name = "Android-A1"
include(":app")