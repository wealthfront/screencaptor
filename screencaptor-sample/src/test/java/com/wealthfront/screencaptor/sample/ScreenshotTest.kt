package com.wealthfront.screencaptor.sample

import android.graphics.BitmapFactory.decodeByteArray
import android.graphics.BitmapFactory.decodeFile
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.wealthfront.screencaptor.ScreenCaptor
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode
import com.wealthfront.screencaptor.sample.R as AppRes

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class ScreenshotTest {

  @Rule
  @JvmField
  val folder = TemporaryFolder()

  @get:Rule
  var activityTestRule: ActivityScenarioRule<SampleActivity> = ActivityScenarioRule(SampleActivity::class.java)

  @After
  fun cleanUpScreenshots() {
    folder.root.deleteRecursively()
  }

  @Test
  fun takeScreenshot_dialog() {
    onView(withId(AppRes.id.showDialog)).perform(click())

    val activityScenario = launchActivity<SampleActivity>()

    Espresso.onIdle()
    ScreenCaptor.takeScreenshot(
      activityScenario = activityScenario,
      screenshotName = "screenshot_dialog",
      screenshotDirectory = folder.root.path
    )

    val screenshot = folder.root.listFiles()!!.find { it.name.contains("screenshot_dialog") }!!
    val actual = decodeFile(screenshot.path)


    val expectedBytes = getInstrumentation().context.resources.openRawResource(AppRes.raw.dialog)
      .readBytes()
    val expected = decodeByteArray(expectedBytes, 0, expectedBytes.size)
    assertTrue(actual.sameAs(expected))
  }
}