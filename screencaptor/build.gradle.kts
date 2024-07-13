plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose.compiler)
}

apply(from = "../gradle/gradle-mvn-push.gradle")
apply(from = "../gradle/android-module.gradle")

android {
    namespace = "com.wealthfront.screencaptor"
    resourcePrefix = "screencaptor"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        compose = true
    }

    publishing {
        singleVariant("release") {
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.recyclerview)
    implementation(libs.screenshotty)
    implementation(libs.espresso.core)
    implementation(libs.espresso.contrib)
    implementation(libs.appcompat)

    testImplementation(libs.robolectric)
    testImplementation(libs.androidx.test)
    testImplementation(libs.androidx.test.ktx)
    testImplementation(libs.junit)
    testImplementation(libs.truth)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.test.junit)
    testImplementation(libs.compose.junit4)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.foundation)
    implementation(libs.compose.ui)
    implementation(libs.compose.tooling)
    implementation(libs.compose.tooling.preview)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    implementation(libs.compose.manifest)
    implementation(libs.compose.junit4)
}