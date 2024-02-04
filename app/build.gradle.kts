plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.booking"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.booking"
        minSdk = 27
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val roomVersion = "2.6.1"
    ksp("androidx.room:room-compiler:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")

    val hiltVersion = "2.48.1"
    implementation("com.google.dagger:hilt-android:$hiltVersion")
    kapt("com.google.dagger:hilt-android-compiler:$hiltVersion")
    kapt("androidx.hilt:hilt-compiler:1.1.0")

    val hiltCompilerVersion = "1.1.0"
    ksp("androidx.hilt:hilt-compiler:${hiltCompilerVersion}")
    implementation("androidx.hilt:hilt-navigation-compose:${hiltCompilerVersion}")
    implementation("androidx.hilt:hilt-work:${hiltCompilerVersion}")

    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")

    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")

    implementation("com.google.android.material:material:1.11.0")

    implementation("androidx.navigation:navigation-ui-ktx:2.7.6")
    api("androidx.navigation:navigation-fragment-ktx:2.7.6")

    implementation("androidx.datastore:datastore-preferences:1.0.0")
}

kapt {
    correctErrorTypes = true
}