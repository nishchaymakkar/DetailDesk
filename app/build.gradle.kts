plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")

}

android {
    namespace = "com.sampleproductapp.detaildesk"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.sampleproductapp.detaildesk"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)

    implementation(libs.material)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.runtime.livedata)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //dagger
    implementation (libs.hilt.android)

    implementation(libs.androidx.hilt.navigation.compose)
    kapt (libs.hilt.compiler)




    implementation( libs.androidx.material)
    implementation(libs.androidx.material.icons.extended)
    implementation(kotlin("script-runtime"))

    implementation( libs.ui.graphics )// Or the latest version
    implementation(libs.coil.compose)
    implementation( libs.androidx.palette.ktx)

    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.core.splashscreen)
    // Retrofit with Kotlin serialization Converter

    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.coil3.coil.compose)
    // Retrofit
    implementation(libs.retrofit)
// Retrofit with Scalar Converter
    implementation(libs.converter.scalars)
    implementation(libs.converter.gson)
    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)
    coreLibraryDesugaring (libs.desugar.jdk.libs)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.swiperefresh)

    // For Play Integrity (Recommended)
    implementation (libs.firebase.appcheck.playintegrity)
    implementation(libs.haze.jetpack.compose)
    implementation(libs.logging.interceptor)
    implementation( libs.androidx.compose.animation)
    implementation (libs.androidx.camera.core)
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle)
    implementation (libs.androidx.camera.view)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.accompanist.pager)
    implementation (libs.androidx.paging.runtime.ktx)

    implementation (libs.androidx.paging.compose)
    implementation (libs.androidx.datastore.preferences)

    implementation(libs.androidx.room.ktx)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.paging)

}