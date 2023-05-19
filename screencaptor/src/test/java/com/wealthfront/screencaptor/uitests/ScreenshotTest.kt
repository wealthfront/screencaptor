package com.wealthfront.screencaptor.uitests

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.wealthfront.screencaptor.R
import com.wealthfront.screencaptor.SampleActivity
import com.wealthfront.screencaptor.ScreenCaptor
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
    activityScenario.onActivity { activity ->
      ScreenCaptor.takeScreenshot(
        activity = activity,
        screenshotName = "screenshot_dialog",
        screenshotDirectory = screenShotDirectory
      )
    }

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
    }
  }
}