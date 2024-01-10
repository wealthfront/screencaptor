plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

apply(from = "../gradle/gradle-mvn-push.gradle")
apply(from = "../gradle/android-module.gradle")

android {
    namespace = "com.wealthfront.screencaptor"

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
    testImplementation(project(":screencaptor-test"))

    androidTestImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(libs.espresso.contrib)
    androidTestImplementation(project(":screencaptor-test"))
}