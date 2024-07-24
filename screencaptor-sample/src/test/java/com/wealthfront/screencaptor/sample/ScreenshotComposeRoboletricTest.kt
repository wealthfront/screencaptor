package com.wealthfront.screencaptor.sample

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.graphics.BitmapFactory.decodeByteArray
import android.graphics.BitmapFactory.decodeFile
import android.os.Environment
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.rule.GrantPermissionRule
import com.wealthfront.screencaptor.ScreenCaptor
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import org.robolectric.shadows.ShadowPixelCopy
import java.io.File

@RunWith(RobolectricTestRunner::class)
class ScreenshotComposeRoboletricTest {

    private val screenShotDirectory: String =
        "${getInstrumentation().targetContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath}/screenshots"

    @get:Rule
    var activityTestRule: ActivityScenarioRule<SampleActivity> =
        ActivityScenarioRule(SampleActivity::class.java)

    @get:Rule
    var permissionsRule = GrantPermissionRule.grant(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

    @get:Rule
    val composeTestRule = createComposeRule()

    @After
    fun cleanUpScreenshots() {
        File(screenShotDirectory).deleteRecursively()
    }

    @GraphicsMode(GraphicsMode.Mode.NATIVE)
    @Test
    fun takeScreenshot__roboletric_compose() {
        composeTestRule.setContent {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .background(Color.White)
            ) {
                Text(text = "Welcome to wealthfront")
                Image(
                    painter = painterResource(id = R.drawable.add_accounts),
                    modifier = Modifier
                        .size(48.dp)
                        .padding(top = 24.dp),
                    contentDescription = ""
                )
            }

        }
        composeTestRule.onNodeWithText("Welcome to wealthfront").assertExists()

        ScreenCaptor.takeScreenshot(
            composeRule = composeTestRule,
            screenshotName = "compose",
            screenshotDirectory = screenShotDirectory
        )


        compareScreenshots("compose", R.raw.compose_unknown_robolectric)
    }


    private fun compareScreenshots(
        actualScreenShotName: String,
        @RawRes expectedScreenShotId: Int
    ) {
        val screenshot = File(screenShotDirectory).listFiles()!!
            .find { it.name.contains(actualScreenShotName) }!!
        val actual = decodeFile(screenshot.path)
        val expectedBytes =
            getInstrumentation().context.resources.openRawResource(expectedScreenShotId).readBytes()
        val expected = decodeByteArray(expectedBytes, 0, expectedBytes.size)
        assertTrue(actual.sameAs(expected))
    }
}
