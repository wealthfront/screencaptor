plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

apply(from = "../gradle/gradle-mvn-push.gradle")

android {
    this.compileSdk = 33

    defaultConfig {
        this.minSdk = 23
        this.targetSdk = 33
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
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.21")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.recyclerview:recyclerview:1.3.0")

    testImplementation("org.robolectric:robolectric:4.10")
    testImplementation("org.mockito:mockito-core:2.23.4")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.google.truth:truth:1.0")

    androidTestImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("androidx.test:rules:1.5.0") {
        exclude(module = "support-annotations")
    }
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1") {
        exclude(group = "androidx.annotation", module = "annotation")
        exclude(group = "junit", module = "junit-dep")
        exclude(group = "com.google.code.findbugs")
    }
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1") {
        exclude(group = "com.android.support", module = "appcompat")
        exclude(group = "com.android.support", module = "support-v4")
        exclude(group = "androidx.appcompat", module = "appcompat")
        exclude(group = "androidx.legacy", module = "legacy-support-v4")
        exclude(group = "com.google.android.material", module = "material")
        exclude(group = "androidx.recyclerview", module = "recyclerview")
        exclude(group = "androidx.annotation", module = "annotation")
    }
}