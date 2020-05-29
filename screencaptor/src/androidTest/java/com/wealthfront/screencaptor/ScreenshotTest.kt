package com.wealthfront.screencaptor

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import androidx.test.rule.GrantPermissionRule
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ScreenshotTest {

  @get:Rule
  var activityTestRule : ActivityTestRule<SampleActivity> = ActivityTestRule(SampleActivity::class.java)

  @get:Rule
  var permissionsRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

  @Test
  fun takeScreenshot() {
    val rootView = activityTestRule.activity.window.decorView

    ScreenCaptor.takeScreenshot(
      view = rootView,
      screenshotFilename = "sample_screenshot"
    )

    assert(File("sdcard/screenshots/").exists())
    assert(File("sdcard/screenshots/").listFiles()!!.isNotEmpty())
    assert(File("sdcard/screenshots/").listFiles()!!.find { it.name.contains("sample_screenshot") }!!.exists())
  }
}