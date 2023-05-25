plugins {
    id(Plugins.androidLibrary)
    id(Plugins.kotlinAndroid)
}

apply(from = "../gradle/gradle-mvn-push.gradle")

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

    sourceSets {
        this.getByName("androidTest"){
            this.java.srcDir("src/sharedTest/java")
            this.manifest.srcFile("src/sharedTest/AndroidManifest.xml")
        }
        this.getByName("test"){
            this.java.srcDir("src/sharedTest/java")
            this.manifest.srcFile("src/sharedTest/AndroidManifest.xml")
        }
    }
}

dependencies {
    implementation(Dependencies.recyclerView)
    implementation(Dependencies.screenshotty)
    implementation(Dependencies.espressoCore)
    implementation(Dependencies.espressoContrib)
    implementation("androidx.appcompat:appcompat:1.5.1")

    testImplementation(Dependencies.robolectric)
    testImplementation(Dependencies.testCore)
    testImplementation(Dependencies.testCoreKtx)
    testImplementation(Dependencies.junit)
    testImplementation(Dependencies.truth)
    testImplementation(Dependencies.espressoCore)
    testImplementation(Dependencies.espressoContrib)
    testImplementation(Dependencies.testRules)
    testImplementation(Dependencies.testExtJunit)

    androidTestImplementation(Dependencies.junit)
    androidTestImplementation(Dependencies.testExtJunit)
    androidTestImplementation(Dependencies.testRules)
    androidTestImplementation(Dependencies.espressoCore)
    androidTestImplementation(Dependencies.espressoContrib)
}