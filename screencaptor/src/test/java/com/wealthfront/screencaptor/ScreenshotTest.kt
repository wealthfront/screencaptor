package com.wealthfront.screencaptor

import android.graphics.BitmapFactory
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.wealthfront.screencaptor.test.R
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ScreenshotTest {

  private val screenShotDirectory: String = "build/test-results/screenshots"

  @get:Rule
  var activityTestRule: ActivityScenarioRule<SampleActivity> = ActivityScenarioRule(SampleActivity::class.java)

  @After
  fun cleanUpScreenshots() {
    File(screenShotDirectory).deleteRecursively()
  }

  @Test
  fun takeScreenshot_dialog() {
    onView(withId(R.id.showDialog)).perform(click())

    val activityScenario = launchActivity<SampleActivity>()

    Espresso.onIdle()
    ScreenCaptor.takeScreenshot(
      activityScenario = activityScenario,
      screenshotName = "screenshot_dialog",
      screenshotDirectory = screenShotDirectory
    )

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      val screenshot = File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_dialog") }!!
      val actual = BitmapFactory.decodeFile(screenshot.path)
      val expectedBytes = InstrumentationRegistry.getInstrumentation().context.resources.openRawResource(R.raw.dialog).readBytes()
      val expected = BitmapFactory.decodeByteArray(expectedBytes, 0, expectedBytes.size)
      assertTrue(actual.sameAs(expected))
    }
  }
}