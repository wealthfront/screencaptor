package com.wealthfront.screencaptor

import android.app.Activity
import android.app.Application
import android.os.Looper.getMainLooper
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows.shadowOf

@RunWith(RobolectricTestRunner::class)
class ScreenCaptorTest {

  @Rule
  @JvmField
  val folder = TemporaryFolder(
    getApplicationContext<Application>().getExternalFilesDir("screenshots")
  )

  private val activityController = Robolectric.buildActivity(Activity::class.java)

  @Test
  fun takeScreenshot() {
    val activity = activityController.setup().get()
    val sampleView = LayoutInflater.from(activity).inflate(R.layout.screenshot_test, null) as ViewGroup
    activity.addContentView(sampleView, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))
    shadowOf(getMainLooper()).idle()
    ScreenCaptor.takeScreenshot(
      activity = activity,
      screenshotName = "sample_screenshot",
      screenshotDirectory = folder.root.path
    )
  }
}