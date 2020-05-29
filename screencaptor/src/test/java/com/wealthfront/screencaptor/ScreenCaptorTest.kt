package com.wealthfront.screencaptor

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class ScreenCaptorTest {

  @Rule @JvmField val folder = TemporaryFolder()

  private lateinit var sampleView: View

  @Before
  fun setUp() {
    val activity = Robolectric.buildActivity(Activity::class.java).create().get()
    sampleView = LayoutInflater.from(activity).inflate(R.layout.screenshot_test, null) as ViewGroup
  }

  @Test
  fun takeScreenshot_withoutDrawing() {
    try {
      ScreenCaptor.takeScreenshot(
        view = sampleView,
        screenshotFilename = "sample_screenshot",
        screenshotDirectory = folder.root.path
      )
    } catch (illegalStateException: IllegalStateException) {
      assertThat(illegalStateException.message).contains("has no height or width")
      assertThat(illegalStateException.message).contains("currently displayed activity?")
    }
  }
}