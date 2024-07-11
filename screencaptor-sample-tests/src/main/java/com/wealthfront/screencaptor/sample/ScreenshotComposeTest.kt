package com.wealthfront.screencaptor.sample

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.graphics.BitmapFactory.decodeByteArray
import android.graphics.BitmapFactory.decodeFile
import android.os.Environment
import android.view.View
import android.widget.TextView
import androidx.annotation.RawRes
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatTextView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers.Visibility.VISIBLE
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.wealthfront.screencaptor.RecyclerViewMatchers
import com.wealthfront.screencaptor.ScreenCaptor
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
import com.wealthfront.screencaptor.sample.R as AppRes
import com.wealthfront.screencaptor.sample.test.R as TestRes

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
        Column(
          modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .padding(24.dp),
          verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
          DemoUI()
        }
    }
    composeTestRule.onNodeWithText("Welcome to wealthfront").assertExists()

    ScreenCaptor.takeScreenshot(
      composeRule = composeTestRule,
      screenshotName = "compose",
      screenshotDirectory = screenShotDirectory
    )

    compareScreenshots("compose", TestRes.raw.compose_pixe7)
  }

  private fun compareScreenshots(actualScreenShotName: String, @RawRes expectedScreenShotId: Int) {
    val screenshot = File(screenShotDirectory).listFiles()!!.find { it.name.contains(actualScreenShotName) }!!
    val actual = decodeFile(screenshot.path)
    val expectedBytes = getInstrumentation().context.resources.openRawResource(expectedScreenShotId).readBytes()
    val expected = decodeByteArray(expectedBytes, 0, expectedBytes.size)
    assertTrue(actual.sameAs(expected))
  }
}