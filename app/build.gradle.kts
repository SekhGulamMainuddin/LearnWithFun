plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    kotlin("plugin.serialization") version "1.8.0"
    id("kotlin-parcelize")
    id("com.google.devtools.ksp")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.sekhgmainuddin.learnwithfun"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.sekhgmainuddin.learnwithfun"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
    kapt {
        correctErrorTypes = true
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    // Firebase an ML Kit
    implementation("com.google.firebase:firebase-messaging-ktx:23.3.1")
    implementation("com.google.firebase:firebase-ml-vision:24.1.0")
    implementation("com.google.firebase:firebase-ml-vision-face-model:20.0.2")
    implementation("com.google.android.gms:play-services-vision:20.1.3")
    implementation("com.google.android.gms:play-services-vision-common:19.1.3")
    implementation("com.google.android.gms:play-services-vision-face-contour-internal:16.1.0")

    // Easy Permissions
    implementation("pub.devrel:easypermissions:3.0.0")

    // Android Graph and Charts using MPAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // Exoplayer
    implementation("androidx.media3:media3-exoplayer:1.1.1")
    implementation("androidx.media3:media3-ui:1.1.1")
    implementation("androidx.media3:media3-common:1.1.1")
    implementation("androidx.media3:media3-session:1.1.1")

    // Camera X
    val cameraXVersion = "1.4.0-alpha02"
    implementation("androidx.camera:camera-camera2:$cameraXVersion")
    implementation("androidx.camera:camera-lifecycle:$cameraXVersion")
    implementation("androidx.camera:camera-view:$cameraXVersion")

    // Agora
    implementation("io.agora.rtc:full-sdk:4.0.1")

    // Kotlin Serialization
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")

    // Retrofit and OkHttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    // Glide
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.14.2")
    implementation("com.github.bumptech.glide:okhttp3-integration:4.15.1")

    // Room Database
    val roomVersion = "2.6.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    annotationProcessor("androidx.room:room-compiler:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    androidTestImplementation("androidx.room:room-testing:$roomVersion")

    // Work Manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.hilt:hilt-work:1.1.0")

    // Dagger-Hilt
    kapt("androidx.hilt:hilt-compiler:1.1.0")
    implementation("androidx.hilt:hilt-navigation-fragment:1.1.0")
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-compiler:2.48.1")

    // Country Picker
    implementation("com.hbb20:ccp:2.7.2")

    // Lottie Animation
    implementation("com.airbnb.android:lottie:5.2.0")

    // Android Layouts, Navigation and Fragments
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    // ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    // Kotlin
    implementation("androidx.annotation:annotation:1.7.0")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

}