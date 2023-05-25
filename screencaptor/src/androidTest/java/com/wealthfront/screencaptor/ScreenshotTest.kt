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
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import org.hamcrest.Description
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
          ContentMutator("dialogic"),
          { it.inRoot(RootMatchers.isDialog()) }
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

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
          ContentMutator("Good boy")
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withId(R.id.messageList)).check(matches(hasItemWithText("Corgi")))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_recyclerview") }!!.exists())
    }
  }

  fun hasItemWithText(text: String) = object : BoundedMatcher<View, View>(View::class.java) {
    override fun describeTo(description: Description) {
      description.appendText("Searching for item with: $text")
    }

    override fun matchesSafely(item: View): Boolean {
      if (item is RecyclerView) {
        for (i in 0 until item.childCount) {
          val childView = item.findViewHolderForAdapterPosition(i)!!.itemView
          if (withText(text).matches(childView)) {
            return true
          }
        }
      }
      return false
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
          ContentMutator("Shorter text")
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
      .check(matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      assertTrue(File(screenShotDirectory).listFiles()!!.find { it.name.contains("screenshot_no_logo") }!!.exists())
    }
  }
}