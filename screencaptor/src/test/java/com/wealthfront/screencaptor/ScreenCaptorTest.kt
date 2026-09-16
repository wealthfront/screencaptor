package com.wealthfront.screencaptor

import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import com.google.common.truth.Truth.assertThat
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

@RunWith(RobolectricTestRunner::class)
class ScreenCaptorTest {

  @get:Rule
  val temporaryFolder = TemporaryFolder()

  @After
  fun unregisterIdlingResources() {
    IdlingRegistry.getInstance().resources.toList().forEach { resource ->
      IdlingRegistry.getInstance().unregister(resource)
    }
  }

  @Test
  fun takeScreenshot_releasesIdlingResourceAndRethrows_whenDirectoryCreationFails() {
    val blockingFile = temporaryFolder.newFile("not-a-directory")
    val screenshotDirectory = File(blockingFile, "screenshots").absolutePath
    val scenario = ActivityScenario.launch(AppCompatActivity::class.java)
    scenario.use { scenario ->
      val exception = assertThrows(IllegalStateException::class.java) {
        ScreenCaptor.takeScreenshot(
          activityScenario = scenario,
          screenshotName = "test",
          screenshotDirectory = screenshotDirectory,
        )
      }

      assertThat(exception).hasMessageThat().isEqualTo(
        "Failed to create screenshot directory for path: $screenshotDirectory"
      )
      assertThat(IdlingRegistry.getInstance().resources).isEmpty()
    }
  }
}
