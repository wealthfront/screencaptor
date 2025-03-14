package com.wealthfront.screencaptor.sample

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.graphics.BitmapFactory.decodeByteArray
import android.graphics.BitmapFactory.decodeFile
import android.os.Environment
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.wealthfront.screencaptor.ScreenCaptor
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode
import java.io.File
import com.wealthfront.screencaptor.sample.R as AppRes

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ScreenshotTest {

  private val screenShotDirectory: String =
    "${getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}/screenshots"

  @get:Rule
  var activityTestRule: ActivityScenarioRule<SampleActivity> =
    ActivityScenarioRule(SampleActivity::class.java)

  @get:Rule
  var permissionsRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

  @After
  fun cleanUpScreenshots() {
    File(screenShotDirectory).deleteRecursively()
  }

  @Test
  fun takeScreenshot_dialog() {
    onView(withId(AppRes.id.showDialog)).perform(click())

    val activityScenario = launchActivity<SampleActivity>()

    Espresso.onIdle()
    ScreenCaptor.takeScreenshot(
      activityScenario = activityScenario,
      screenshotName = "screenshot_dialog",
      screenshotDirectory = screenShotDirectory,
    )

    val screenshot = File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_dialog") }!!
    val actual = decodeFile(screenshot.path)


    val expectedBytes = getInstrumentation().context.resources.openRawResource(AppRes.raw.dialog)
      .readBytes()
    val expected = decodeByteArray(expectedBytes, 0, expectedBytes.size)
    assertTrue(actual.sameAs(expected))
  }
}