plugins {
  alias(libs.plugins.androidApplication)
  alias(libs.plugins.kotlinAndroid)
}

apply(from = "../gradle/android-module.gradle")

android {
  defaultConfig {
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  namespace = "com.wealthfront.screencaptor.sample"
}

dependencies {
  implementation(project(":screencaptor"))
  implementation(libs.recyclerview)
  implementation(libs.appcompat)
  testImplementation(project(":internal-test-support"))

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