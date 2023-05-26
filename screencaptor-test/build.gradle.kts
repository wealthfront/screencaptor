plugins {
  id(Plugins.androidLibrary)
  id(Plugins.kotlinAndroid)
}

dependencies {
  implementation(Dependencies.recyclerView)
  implementation(Dependencies.espressoCore)
  implementation(Dependencies.espressoContrib)
  implementation(Dependencies.appCompat)
}

android {
  this.compileSdk = Versions.compileSdkVersion

  defaultConfig {
    this.minSdk = Versions.minSdkVersion
    this.targetSdk = Versions.targetSdkVersion
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