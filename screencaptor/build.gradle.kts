plugins {
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.kotlinAndroid)
}

apply(from = "../gradle/gradle-mvn-push.gradle")
apply(from = "../gradle/android-module.gradle")

android {
    namespace = "com.wealthfront.screencaptor"
    resourcePrefix = "screencaptor"

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    testImplementation(libs.espresso.core)
    testImplementation(libs.espresso.contrib)
    testImplementation(libs.androidx.test.rules)
    testImplementation(libs.androidx.test.junit)
}