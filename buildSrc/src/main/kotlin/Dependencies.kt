
import Versions.appCompatVersion
import Versions.junitVersion
import Versions.recyclerViewVersion
import Versions.robolectricVersion
import Versions.screenshottyVersion
import Versions.testCoreVersion
import Versions.truthVersion

object Plugins {
    const val androidLibrary = "com.android.library"
    const val kotlinAndroid = "org.jetbrains.kotlin.android"
}

object Dependencies {
    const val recyclerView = "androidx.recyclerview:recyclerview:$recyclerViewVersion"
    const val appCompat = "androidx.appcompat:appcompat:$appCompatVersion"
    const val screenshotty = "eu.bolt:screenshotty:$screenshottyVersion"

    const val robolectric = "org.robolectric:robolectric:$robolectricVersion"
    const val testCore = "androidx.test:core:$testCoreVersion"
    const val testCoreKtx = "androidx.test:core-ktx:$testCoreVersion"
    const val junit = "junit:junit:$junitVersion"
    const val truth = "com.google.truth:truth:$truthVersion"

    const val testExtJunit = "androidx.test.ext:junit:1.1.5"
    const val testRules = "androidx.test:rules:1.5.0"
    const val espressoCore = "androidx.test.espresso:espresso-core:3.5.1"
    const val espressoContrib = "androidx.test.espresso:espresso-contrib:3.5.1"
}

