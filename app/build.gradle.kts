plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.marneux.marneweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.marneux.marneweather"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName("release") {
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
        // posible solucion java.time api sdk <26 https://stackoverflow.com/questions/63789699/iscorelibrarydesugaringenabled-not-works-in-gradle-kotlin-dsl-kts
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.7"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // core
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")

    // java.time support for api's < Android O
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    // compose
    val composeBom = platform("androidx.compose:compose-bom:2023.08.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    implementation("androidx.compose.material3:material3")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation("androidx.compose.material:material-android:1.5.4")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    // image loading
    implementation("io.coil-kt:coil-compose:2.4.0")
    implementation("io.coil-kt:coil-gif:2.4.0")
    implementation("io.coil-kt:coil-svg:2.4.0")

    // placeholders
    implementation("com.google.accompanist:accompanist-placeholder-material3:0.30.1")

    // splashscreen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // room
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    // esta sin actualizar por problemas de compatibilidades
    ksp("androidx.room:room-compiler:2.5.1")

    // logging
    implementation("com.jakewharton.timber:timber:5.0.1")

    // coroutines support for Task<T>
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // location services
    implementation("com.google.android.gms:play-services-location:21.0.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // "await()" support for Task<T>
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    //Gson
    implementation("com.google.code.gson:gson:2.9.0")

    // work manager
    implementation("androidx.work:work-runtime-ktx:2.8.1")

    // junit
    testImplementation("junit:junit:4.13.2")

    // mockito
    testImplementation("org.mockito.kotlin:mockito-kotlin:5.0.0")

    // koin dependency injection
    val koinVersion = "3.4.0"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    implementation("io.insert-koin:koin-androidx-workmanager:$koinVersion")
}