package com.wealthfront.screencaptor

import android.os.Environment
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.wealthfront.screencaptor.views.modifier.TextViewDataModifier
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File

@RunWith(AndroidJUnit4::class)
class ScreenshotTest {

  private val screenShotDirectory: String =
    "${getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}/screenshots"

  @get:Rule
  var activityTestRule: ActivityScenarioRule<SampleActivity> = ActivityScenarioRule(SampleActivity::class.java)

  @After
  fun cleanUpScreenshots() {
    File(screenShotDirectory).deleteRecursively()
  }

  @Test
  fun takeScreenshot_dialog() {
    onView(withId(R.id.showDialog)).perform(click())

    activityTestRule.scenario.onActivity { activity ->
      ScreenCaptor.takeScreenshot(
        activity = activity,
        screenshotName = "screenshot_dialog",
        screenshotDirectory = screenShotDirectory
      )
    }

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_dialog") }!!.exists())
    }
  }

  @Test
  fun takeScreenshot_modify() {
    activityTestRule.scenario.onActivity { activity ->
      ScreenCaptor.takeScreenshot(
        activity = activity,
        screenshotName = "screenshot_change_text",
        viewModifiers = setOf(TextViewDataModifier(R.id.textView, "Some shorter sample data")),
        screenshotDirectory = screenShotDirectory
      )
    }

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(
        File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_change_text") }!!.exists()
      )
    }
  }

  @Test
  fun takeScreenshot_exclude() {
    activityTestRule.scenario.onActivity { activity ->
      ScreenCaptor.takeScreenshot(
        activity = activity,
        screenshotName = "screenshot_no_logo",
        viewIdsToExclude = setOf(R.id.wealthfrontIcon),
        screenshotDirectory = screenShotDirectory
      )
    }

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_no_logo") }!!.exists())
    }
  }
}