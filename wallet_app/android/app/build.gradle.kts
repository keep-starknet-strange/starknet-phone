import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
    id("org.jmailen.kotlinter")
}

android {
    namespace = "com.example.walletapp"
    compileSdk = 34

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



        buildConfigField("String", "DEMO_RPC_URL", "\"https://starknet-mainnet.g.alchemy.com/starknet/version/rpc/v0_7/${properties.getProperty("KEY_NAME")}\"")
        buildConfigField("String", "DEMO_ACCOUNT_ADDRESS", "\"0x02dc260794e4c2eeae87b1403a88385a72c18a5844d220b88117b2965a8cf3a5\"")
        buildConfigField("String", "DEMO_PRIVATE_KEY", "\"rFAP8fkTAz9TmYw8_V5Fyzxi-WSoQdhk\"")
        buildConfigField("String", "DEMO_RECIPIENT_ACCOUNT_ADDRESS", "\"0xc1c7db92d22ef773de96f8bde8e56c85\"")
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}
dependencies {

    implementation("com.swmansion.starknet:starknet:0.12.1@aar"){
        isTransitive = true
    }

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
    // or Material Design 2
    implementation(libs.androidx.material)

    // Retrofit for network requests
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)
    implementation(libs.androidx.hilt.navigation.compose)

    //dagger
    implementation(libs.hilt.android)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation (libs.androidx.hilt.navigation.compose.v100alpha03)

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