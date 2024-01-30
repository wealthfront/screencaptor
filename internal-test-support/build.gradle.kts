plugins {
  alias(libs.plugins.androidLibrary)
  alias(libs.plugins.kotlinAndroid)
}

apply(from = "../gradle/android-module.gradle")

android {
  namespace = "com.wealthfront.screencaptor"
}

dependencies {
  implementation(libs.recyclerview)
  implementation(libs.espresso.core)
  implementation(libs.espresso.contrib)
}