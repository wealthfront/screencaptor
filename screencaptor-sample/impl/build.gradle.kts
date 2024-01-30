plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinAndroid)
}

apply(from = "../../gradle/android-module.gradle")

android {
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  namespace = "com.wealthfront.screencaptor.sample.impl"
}

dependencies {
  implementation(project(":screencaptor"))

  implementation(libs.recyclerview)
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