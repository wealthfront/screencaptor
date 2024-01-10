plugins {
  id(Plugins.androidLibrary)
  id(Plugins.kotlinAndroid)
}

dependencies {
  implementation(libs.recyclerview)
  implementation(libs.espresso.core)
  implementation(libs.espresso.contrib)
  implementation(libs.appcompat)
}

apply(from = "../gradle/android-module.gradle")

android {
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  namespace = "com.wealthfront.screencaptor.test"
}