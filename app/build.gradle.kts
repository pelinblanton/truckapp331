plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.example.truckapp331"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.truckapp331"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.identity.doctypes.jvm)
    // ✅ Define the Compose BOM first
    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")

    // ✅ Then include it
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Compose core
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.activity:activity-compose:1.8.2")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.5")

    // Optional icons
    implementation("androidx.compose.material:material-icons-extended")

    // Tooling
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    //Google maps
    //implementation("com.google.maps.android:maps-compose:2.11.4")
    //implementation("com.google.android.gms:play-services-maps:18.2.0")
}
