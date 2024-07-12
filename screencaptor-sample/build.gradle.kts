plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
  alias(libs.plugins.compose.compiler)
}

apply(from = "../gradle/android-module.gradle")

android {
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  namespace = "com.wealthfront.screencaptor.sample"

  buildFeatures {
    compose = true
  }
}

dependencies {
  implementation(project(":screencaptor"))
  implementation(libs.recyclerview)
  implementation(libs.appcompat)
  testImplementation(project(":internal-test-support"))

  implementation(platform(libs.compose.bom))
  implementation(libs.compose.foundation)
  implementation(libs.compose.ui)
  implementation(libs.compose.tooling)
  implementation(libs.compose.tooling.preview)
  implementation(libs.compose.material)
  implementation(libs.compose.material3)
  implementation(libs.compose.manifest)
  implementation(libs.compose.junit4)

  testImplementation(libs.robolectric)
  testImplementation(libs.androidx.test)
  testImplementation(libs.androidx.test.ktx)
  testImplementation(libs.junit)
  testImplementation(libs.truth)
  testImplementation(libs.espresso.core)
  testImplementation(libs.espresso.contrib)
  testImplementation(libs.androidx.test.rules)
  testImplementation(libs.androidx.test.junit)
  testImplementation(libs.compose.junit4)
}