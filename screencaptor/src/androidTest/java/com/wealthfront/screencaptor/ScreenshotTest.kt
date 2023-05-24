package com.wealthfront.screencaptor

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Environment
import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
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

  @get:Rule
  var permissionsRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

  @After
  fun cleanUpScreenshots() {
    File(screenShotDirectory).deleteRecursively()
  }

  @Test
  fun takeScreenshot_dialog() {
    onView(withId(R.id.showDialog)).perform(click())

    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_dialog",
      screenshotDirectory = screenShotDirectory
    )

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_dialog") }!!.exists())
    }
  }

  @Test
  fun takeScreenshot_modify() {
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_change_text",
      viewMutations = setOf(
        ViewMutation(withId(R.id.textView), ContentMutator("Shorter text"))
      ),
      screenshotDirectory = screenShotDirectory
    )

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
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_no_logo",
      viewMutations = setOf(
        ViewMutation(withId(R.id.wealthfrontIcon), VisibilityMutator(View.INVISIBLE))
      ),
      screenshotDirectory = screenShotDirectory
    )

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_no_logo") }!!.exists())
    }
  }
}