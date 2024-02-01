plugins {
  alias(libs.plugins.androidTest)
  alias(libs.plugins.kotlinAndroid)
}

android {
  namespace = "com.wealthfront.screencaptor.sample.test"
  compileSdk = 34

  defaultConfig {
    minSdk = 26
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
  }
  kotlinOptions {
    jvmTarget = "1.8"
  }

  targetProjectPath(":screencaptor-sample")
}

dependencies {
  implementation(project(":screencaptor"))
  implementation(project(":internal-test-support"))

  implementation(libs.androidx.test)
  implementation(libs.androidx.test.ktx)
  implementation(libs.junit)
  implementation(libs.truth)
  implementation(libs.espresso.core)
  implementation(libs.espresso.contrib)
  implementation(libs.androidx.test.rules)
  implementation(libs.androidx.test.junit)
}