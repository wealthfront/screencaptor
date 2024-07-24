package com.wealthfront.screencaptor.sample

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.graphics.BitmapFactory.decodeByteArray
import android.graphics.BitmapFactory.decodeFile
import android.os.Environment
import android.util.Log
import androidx.annotation.RawRes
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.printToString
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.wealthfront.screencaptor.ScreenCaptor
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import com.wealthfront.screencaptor.sample.test.R as TestRes

@OptIn(ExperimentalTestApi::class)
@RunWith(AndroidJUnit4::class)
class ScreenshotComposeTest {

  private val screenShotDirectory: String =
    "${getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}/screenshots"

  @get:Rule
  var activityTestRule: ActivityScenarioRule<SampleActivity> = ActivityScenarioRule(SampleActivity::class.java)

  @get:Rule
  var permissionsRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

  @get:Rule
  val composeTestRule = createComposeRule()

  @After
  fun cleanUpScreenshots() {
    File(screenShotDirectory).deleteRecursively()
  }

  @Test
  fun takeScreenshot_compose() {
    composeTestRule.setContent {
      DemoUI()
    }
    composeTestRule.onNodeWithText("Welcome to wealthfront").assertExists()

    ScreenCaptor.takeScreenshot(
      composeRule = composeTestRule,
      screenshotName = "compose",
      screenshotDirectory = screenShotDirectory
    )

    compareScreenshots("compose", TestRes.raw.compose_pixel7_api33)
  }

  @Test
  fun takeScreenshot_compose_dialogs() {
    composeTestRule.setContent {
      DemoUI(true)
    }
    composeTestRule.onNodeWithText("Welcome to wealthfront").assertExists()
    composeTestRule.onNodeWithText("Add more").performClick()

    //ComposeTestRule.waitForIdle() doesn't wait until the dialog is fully open.
    composeTestRule.waitForIdle()
    Thread.sleep(300)

    ScreenCaptor.takeScreenshot(
      screenshotName = "compose_dialogs",
      screenshotDirectory = screenShotDirectory
    )

    compareScreenshots("compose_dialogs", TestRes.raw.compose_dialogs_pixel7_api33)
  }


  private fun compareScreenshots(actualScreenShotName: String, @RawRes expectedScreenShotId: Int) {
    val screenshot = File(screenShotDirectory).listFiles()!!.find { it.name.contains(actualScreenShotName) }!!
    val actual = decodeFile(screenshot.path)
    val expectedBytes = getInstrumentation().context.resources.openRawResource(expectedScreenShotId).readBytes()
    val expected = decodeByteArray(expectedBytes, 0, expectedBytes.size)
    assertTrue(actual.sameAs(expected))
  }
}