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

  compileOptions {
    sourceCompatibility(JavaVersion.VERSION_1_8)
    targetCompatibility(JavaVersion.VERSION_1_8)
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }

  testOptions.apply {
    animationsDisabled = true
    unitTests.apply {
      isIncludeAndroidResources = true
      isReturnDefaultValues = true
    }
  }

  namespace = "com.wealthfront.screencaptor.test"
}