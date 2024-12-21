import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("org.jmailen.kotlinter")
    kotlin("plugin.serialization") version "2.0.0-RC1"
    alias(libs.plugins.compose.compiler)

}

android {
    namespace = "com.example.walletapp"
    compileSdk = 34

    dataBinding {
        enable = true
    }


    buildFeatures {
        compose = true
        buildConfig = true
    }

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("local.properties").inputStream())

        applicationId = "com.example.walletapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"



        buildConfigField("String", "RPC_URL", "\"${properties.getProperty("RPC_URL")}\"")
        buildConfigField("String", "ACCOUNT_ADDRESS", "\"${properties.getProperty("ACCOUNT_ADDRESS")}\"")
        buildConfigField("String", "PRIVATE_KEY", "\"${properties.getProperty("KEY_NAME")}\"")
    }

    buildTypes {


        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {

    implementation("com.swmansion.starknet:starknet:0.13.0@aar"){
        isTransitive = true
    }

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // Navigation stuff
    implementation(libs.androidx.navigation.compose.v283)
    implementation(libs.androidx.navigation.compose.v283)
    implementation(libs.androidx.navigation.fragment)
    implementation(libs.androidx.navigation.ui)
    implementation(libs.androidx.navigation.dynamic.features.fragment)
    androidTestImplementation(libs.androidx.navigation.testing)


    implementation(libs.androidx.security.crypto.v110alpha06)

    // for data binding
    implementation(libs.common)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val composeBom = platform("androidx.compose:compose-bom:2024.06.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Material Design 3
    implementation(libs.androidx.material3)

    implementation("com.google.android.material:material:1.11.0")

    // Retrofit for network requests
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.hilt.navigation.compose)

    //dagger
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation (libs.androidx.hilt.navigation.compose.v100alpha03)

    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    // image loader
    implementation("io.coil-kt:coil-compose:2.6.0")
    implementation("com.github.skydoves:landscapist-glide:1.3.7")
    implementation("io.coil-kt:coil-gif:2.6.0")

    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Optional - Integration with activities
    implementation(libs.androidx.activity.compose)
    // Optional - Integration with ViewModels
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Optional - Integration with LiveData
    implementation(libs.androidx.runtime.livedata)

    implementation(libs.androidx.navigation.compose)



    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(kotlin("script-runtime"))
}