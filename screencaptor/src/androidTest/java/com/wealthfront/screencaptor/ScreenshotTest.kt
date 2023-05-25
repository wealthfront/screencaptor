package com.wealthfront.screencaptor

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.wealthfront.screencaptor.recyclerviewmutator.RecyclerViewMutationOnItem
import com.wealthfront.screencaptor.recyclerviewmutator.RecyclerViewTextMutator
import com.wealthfront.screencaptor.viewmutator.TextMutator
import com.wealthfront.screencaptor.viewmutator.ViewMutationImpl
import com.wealthfront.screencaptor.viewmutator.VisibilityMutator
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
      viewMutations = setOf(
        ViewMutationImpl(
          withText("I am a Dialog"),
          TextMutator("dialogic")
        ) { it.inRoot(isDialog()) }
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withText("I am a Dialog"))
      .inRoot(isDialog())
      .check(matches(isDisplayed()))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_dialog") }!!.exists())
    }
  }

  @Test
  fun takeScreenshot_recyclerView() {
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_recyclerview",
      viewMutations = setOf(
        RecyclerViewMutationOnItem<RecyclerView.ViewHolder, TextView, CharSequence>(
          withId(R.id.messageList),
          withText("Corgi"),
          RecyclerViewTextMutator("Good boy")
        ),
        RecyclerViewMutationOnItem(
          withId(R.id.messageList),
          withText("Mastiff"),
          RecyclerViewTextMutator("Bad dog")
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withId(R.id.messageList))
      .check(matches(RecyclerViewMatchers.hasItemWithText("Corgi")))
    onView(withId(R.id.messageList))
      .check(matches(RecyclerViewMatchers.hasItemWithText("Mastiff")))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_recyclerview") }!!.exists())
    }
  }

  @Test
  fun takeScreenshot_modify() {
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_change_text",
      viewMutations = setOf(
        ViewMutationImpl(
          withId(R.id.textView),
          TextMutator("Shorter text")
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withText("Some sample data which is really long, so long that it wraps to another line and maybe even three lines"))
      .check(matches(isDisplayed()))

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
        ViewMutationImpl(
          withId(R.id.wealthfrontIcon),
          VisibilityMutator(View.INVISIBLE)
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withId(R.id.wealthfrontIcon))
      .check(matches(withEffectiveVisibility(VISIBLE)))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_no_logo") }!!.exists())
    }
  }
}