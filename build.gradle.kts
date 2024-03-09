
plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.0" apply false
    id("com.google.devtools.ksp") version "1.9.0-1.0.12" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

buildscript {
    repositories {
        mavenCentral()
        google()
    }
    dependencies {
        val navVersion = "2.7.7"
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.48.1")
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
    }
}