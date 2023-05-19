package com.wealthfront.screencaptor.uitest

import android.os.Environment
import android.util.Log
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import com.squareup.rx2.idler.Rx2Idler
import com.wealthfront.screencaptor.SampleActivity
import com.wealthfront.screencaptor.ScreenCaptor
import io.reactivex.plugins.RxJavaPlugins
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.GraphicsMode
import java.io.File

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// @LooperMode(LooperMode.Mode.LEGACY)
class ScreenshotTest {

  private val screenShotDirectory: String =
    "${getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}/screenshots"

  // @get:Rule
  // var activityTestRule: ActivityScenarioRule<SampleActivity> = ActivityScenarioRule(SampleActivity::class.java)

  @Before
  fun setupRxIdler() {
    RxJavaPlugins.setInitComputationSchedulerHandler(
      Rx2Idler.create("RxJava 2.x Computation Scheduler"));
    RxJavaPlugins.setInitIoSchedulerHandler(
      Rx2Idler.create("RxJava 2.x IO Scheduler"));
  }
  @After
  fun cleanUpScreenshots() {
    // File(screenShotDirectory).deleteRecursively()
  }

  @Test
  fun takeScreenshot_dialog() {
    // onView(withId(R.id.showDialog)).perform(click())

    val activityScenario = launchActivity<SampleActivity>()

    Espresso.onIdle()
    activityScenario.onActivity { activity ->
      ScreenCaptor.takeScreenshot(
        activity = activity,
        screenshotName = "screenshot_dialog",
        screenshotDirectory = "sdcard/screenshots",
        onSuccess =  {
          Log.v("BEER", it.toString())
        },
        onError = {
          Log.v("BEER", it.toString())
        }
      )
    }

    Espresso.onIdle {
      assertTrue(File("sdcard/screenshots").exists())
    }
    // IdlingRegistry.getInstance().unregister(myRes)
  }

  /*
  @Test
  fun takeScreenshot_modify() {
    activityTestRule.scenario.onActivity { activity ->
      ScreenCaptor.takeScreenshot(
        activity = activity,
        screenshotName = "screenshot_change_text",
        viewModifiers = setOf(TextViewDataModifier(R.id.textView, "Some shorter sample data")),
        screenshotDirectory = screenShotDirectory,
        onSuccess = {},
        onError = {}
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
        screenshotDirectory = screenShotDirectory,
        onSuccess = {},
        onError = {}
      )
    }
  }
   */
}