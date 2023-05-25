package com.wealthfront.screencaptor

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.graphics.BitmapFactory
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.annotation.RawRes
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
import com.wealthfront.screencaptor.viewmutator.ImageViewMutator
import com.wealthfront.screencaptor.viewmutator.TextViewMutator
import com.wealthfront.screencaptor.viewmutator.ViewMutationImpl
import com.wealthfront.screencaptor.viewmutator.VisibilityViewMutator
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import com.wealthfront.screencaptor.test.R as TestRes

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
    onView(withId(TestRes.id.showDialog)).perform(click())

    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_dialog",
      viewMutations = setOf(
        ViewMutationImpl(
          withText("I am a Dialog"),
          TextViewMutator("dialogic")
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
      compareScreenshots("screenshot_dialog", TestRes.raw.dialog)
    }
  }

  @Test
  fun takeScreenshot_recyclerView() {
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_recyclerview",
      viewMutations = setOf(
        RecyclerViewMutationOnItem<RecyclerView.ViewHolder, TextView, CharSequence>(
          withId(TestRes.id.messageList),
          withText("Corgi"),
          RecyclerViewTextMutator("Good boy")
        ),
        RecyclerViewMutationOnItem(
          withId(TestRes.id.messageList),
          withText("Mastiff"),
          RecyclerViewTextMutator("Bad dog")
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withId(TestRes.id.messageList))
      .check(matches(RecyclerViewMatchers.hasItemWithText("Corgi")))
    onView(withId(TestRes.id.messageList))
      .check(matches(RecyclerViewMatchers.hasItemWithText("Mastiff")))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      compareScreenshots("screenshot_recyclerview", TestRes.raw.recycler_view)
    }
  }

  @Test
  fun takeScreenshot_modifyText() {
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_change_text",
      viewMutations = setOf(
        ViewMutationImpl(
          withId(TestRes.id.textView),
          TextViewMutator("Shorter text")
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withText("Some sample data which is really long, so long that it wraps to another line and maybe even three lines"))
      .check(matches(isDisplayed()))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      compareScreenshots("screenshot_change_text", TestRes.raw.change_text)
    }
  }

  @Test
  fun takeScreenshot_modifyImage() {
    val newDrawable = getInstrumentation().context.getDrawable(TestRes.drawable.add_accounts)!!
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_change_image",
      viewMutations = setOf(
        ViewMutationImpl(
          withId(TestRes.id.wealthfrontIcon),
          ImageViewMutator(newDrawable)
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      compareScreenshots("screenshot_change_image", TestRes.raw.change_image)
    }
  }

  @Test
  fun takeScreenshot_exclude() {
    ScreenCaptor.takeScreenshot(
      activityScenario = activityTestRule.scenario,
      screenshotName = "screenshot_no_logo",
      viewMutations = setOf(
        ViewMutationImpl(
          withId(TestRes.id.wealthfrontIcon),
          VisibilityViewMutator(View.INVISIBLE)
        )
      ),
      screenshotDirectory = screenShotDirectory
    )

    onView(withId(TestRes.id.wealthfrontIcon))
      .check(matches(withEffectiveVisibility(VISIBLE)))

    Espresso.onIdle {
      assertTrue(File(screenShotDirectory).exists())
      assertTrue(File(screenShotDirectory).listFiles()!!.isNotEmpty())
      compareScreenshots("screenshot_no_logo", TestRes.raw.no_logo)
    }
  }

  private fun compareScreenshots(actualScreenShotName: String, @RawRes expectedScreenShotId: Int) {
    val screenshot = File(screenShotDirectory).listFiles()!!.find { it.name.contains(actualScreenShotName) }!!
    val actual = BitmapFactory.decodeFile(screenshot.path)
    val expectedBytes = getInstrumentation().context.resources.openRawResource(expectedScreenShotId).readBytes()
    val expected = BitmapFactory.decodeByteArray(expectedBytes, 0, expectedBytes.size)
    assertTrue(actual.sameAs(expected))
  }
}