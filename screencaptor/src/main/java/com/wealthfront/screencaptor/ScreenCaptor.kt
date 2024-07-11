package com.wealthfront.screencaptor

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Build.VERSION.SDK_INT
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.captureToImage
import androidx.compose.ui.test.junit4.ComposeTestRule
import androidx.compose.ui.test.onRoot
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.IdlingRegistry
import androidx.test.platform.app.InstrumentationRegistry
import com.wealthfront.screencaptor.ScreenCaptor.takeScreenshot
import com.wealthfront.screencaptor.ScreenshotFormat.PNG
import com.wealthfront.screencaptor.ScreenshotQuality.BEST
import com.wealthfront.screencaptor.globalmutator.CursorHider
import com.wealthfront.screencaptor.globalmutator.ScrollbarHider
import com.wealthfront.screencaptor.globalmutator.ViewTreeMutator
import com.wealthfront.screencaptor.idlingresource.ScreenshotIdlingResource
import eu.bolt.screenshotty.Screenshot
import eu.bolt.screenshotty.ScreenshotActionOrder
import eu.bolt.screenshotty.ScreenshotManagerBuilder
import eu.bolt.screenshotty.util.ScreenshotFileSaver
import java.io.File
import java.io.FileOutputStream
import java.util.Locale.ENGLISH

/**
 * Has the ability to take a screenshot of the current view displayed on the screen using the method [takeScreenshot].
 */
object ScreenCaptor {

  private val SCREENSHOT = javaClass.simpleName
  private val TOP_BAR_HEIGHT = 24
  private const val defaultScreenshotDirectory = "screenshots"
  private val defaultGlobalMutations: Set<GlobalViewMutation> = setOf(CursorHider, ScrollbarHider)

  private fun getScreenshotFile(
    screenshotDirectory: String = defaultScreenshotDirectory,
    screenshotName: String,
    screenshotNameSuffix: String = "",
    screenshotFormat: ScreenshotFormat = PNG,
  ): File {
    if (!File(screenshotDirectory).exists()) {
      Log.d(SCREENSHOT, "Creating directory $screenshotDirectory since it does not exist")
      val screenshotDirsCreated = File(screenshotDirectory).mkdirs()
      assert(screenshotDirsCreated)
    }

    val deviceName = MANUFACTURER.replaceWithUnderscore() + "_" + MODEL.replaceWithUnderscore()
    val screenshotId =
      "${screenshotName.toLowerCase(ENGLISH)}_${deviceName}_${SDK_INT}_$screenshotNameSuffix"
    return File("$screenshotDirectory/$screenshotId.${screenshotFormat.extension}")
  }

  private fun captureScreenshot(
    activity: Activity,
    screenshotFile: File,
    screenshotQuality: ScreenshotQuality = BEST,
    onSuccess: (Screenshot) -> Unit
  ) {
    val screenshotManager = ScreenshotManagerBuilder(activity)
      .withCustomActionOrder(ScreenshotActionOrder.fallbacksFirst())
      .build()

    screenshotManager.makeScreenshot()
      .observe({ screenshot ->
        val fileSaver = ScreenshotFileSaver.create(
          compressFormat = Bitmap.CompressFormat.PNG,
          compressQuality = screenshotQuality.value
        )
        fileSaver.saveToFile(screenshotFile, screenshot)

        onSuccess.invoke(screenshot)
      }, { throwable -> throw throwable })
  }

  private fun captureScreenshot(
    composeRule: ComposeTestRule,
    screenshotFile: File,
    screenshotQuality: ScreenshotQuality = BEST,
    rootMatcher: SemanticsMatcher? = null
  ) {
    val rootNode = if (rootMatcher != null) composeRule.onNode(rootMatcher) else composeRule.onRoot()
    val image = rootNode.captureToImage().asAndroidBitmap()
    FileOutputStream(screenshotFile).use { out ->
      image.compress(Bitmap.CompressFormat.PNG, screenshotQuality.value, out)
    }
  }

  private fun captureScreenshot(
    screenshotFile: File,
    screenshotQuality: ScreenshotQuality = BEST
  ) {
    val instrumentation = InstrumentationRegistry.getInstrumentation()
    val fullScreenshot = instrumentation.uiAutomation.takeScreenshot()
    val density = instrumentation.targetContext.resources.displayMetrics.density
    val topBarHeightPx = (TOP_BAR_HEIGHT * density).toInt()
    val croppedScreenshot = Bitmap.createBitmap(
      fullScreenshot,
      0,
      topBarHeightPx,
      fullScreenshot.width,
      fullScreenshot.height - topBarHeightPx
    )
    FileOutputStream(screenshotFile).use { out ->
      croppedScreenshot.compress(Bitmap.CompressFormat.PNG, screenshotQuality.value, out)
    }
  }

  /**
   * Takes a screenshot. Can be called from the test thread or the main thread.
   *
   * @param activityScenario to be captured as the screenshot and saved on the path provided.
   *
   * @param screenshotName is the name of the file that the screenshot will be saved under.
   * Usually, it's a pretty good idea to have this be pretty descriptive. By default, the name of
   * the screenshot will have the device and sdk information attached to it.
   *
   * @param screenshotNameSuffix is an optional param to add a suffix to the name of the screenshot file.
   *
   * @param viewMutations mutate specific views before capturing a screenshot.
   *
   * @param globalViewMutations mutations that will be applied to the entire view tree.
   *
   * @param screenshotDirectory allows you to specify where the screenshot taken should be saved in
   * the device. Note: On devices above API 29, saving directly to an external storage in not allowed.
   * It's recommended to pass in an app-specific path retrieved from the context e.g.
   * {@code context.filesDir} or {@code context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}.
   *
   * @param screenshotFormat specifies the format of the screenshot file.
   *
   * @param screenshotQuality specifies the level of compression of the screenshot.
   *
   */
  @Synchronized
  fun takeScreenshot(
    activityScenario: ActivityScenario<out AppCompatActivity>,
    screenshotName: String,
    screenshotNameSuffix: String = "",
    viewMutations: Set<ViewMutation> = setOf(),
    globalViewMutations: Set<GlobalViewMutation> = setOf(),
    screenshotDirectory: String = defaultScreenshotDirectory,
    screenshotFormat: ScreenshotFormat = PNG,
    screenshotQuality: ScreenshotQuality = BEST
  ) {
    viewMutations.forEach { viewMutation ->
      with(viewMutation) {
        getViewInteraction().perform(getPerformAction())
      }
    }

    val idlingResource = ScreenshotIdlingResource()
    IdlingRegistry.getInstance().register(idlingResource)
    val screenshotFile = getScreenshotFile(
      screenshotDirectory = screenshotDirectory,
      screenshotName = screenshotName,
      screenshotNameSuffix = screenshotNameSuffix,
      screenshotFormat = screenshotFormat
    )
    activityScenario.onActivity { activity ->
      ViewTreeMutator.Builder()
        .addMutations(defaultGlobalMutations)
        .addMutations(globalViewMutations)
        .build()
        .mutate(activity)

      captureScreenshot(
        activity,
        screenshotFile,
        screenshotQuality
      ) { screenshot ->
        idlingResource.setScreenshotCaptured()
        IdlingRegistry.getInstance().unregister(idlingResource)
      }
    }

    viewMutations.forEach { viewMutation ->
      with(viewMutation) {
        getViewInteraction().perform(getRestoreAction())
      }
    }
  }

  /**
   * Takes a screenshot of a compose node. Not meant to capture PopUp since it is in a different window. Can be called from the test thread or the main thread.
   *
   * @param composeRule is the composeTestRule where the content has been set.
   *
   * @param screenshotName is the name of the file that the screenshot will be saved under.
   * Usually, it's a pretty good idea to have this be pretty descriptive. By default, the name of
   * the screenshot will have the device and sdk information attached to it.
   *
   * @param screenshotNameSuffix is an optional param to add a suffix to the name of the screenshot file.
   *
   * @param screenshotDirectory allows you to specify where the screenshot taken should be saved in
   * the device. Note: On devices above API 29, saving directly to an external storage in not allowed.
   * It's recommended to pass in an app-specific path retrieved from the context e.g.
   * {@code context.filesDir} or {@code context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}.
   *
   * @param screenshotFormat specifies the format of the screenshot file.
   *
   * @param screenshotQuality specifies the level of compression of the screenshot.
   *
   * @param rootMatcher specifies the node to capture. Captures root if null
   */
  @Synchronized
  fun takeScreenshot(
    composeRule: ComposeTestRule,
    screenshotName: String,
    screenshotNameSuffix: String = "",
    screenshotDirectory: String = defaultScreenshotDirectory,
    screenshotFormat: ScreenshotFormat = PNG,
    screenshotQuality: ScreenshotQuality = BEST,
    rootMatcher: SemanticsMatcher? = null
  ) {
    val screenshotFile = getScreenshotFile(
      screenshotDirectory = screenshotDirectory,
      screenshotName = screenshotName,
      screenshotNameSuffix = screenshotNameSuffix,
      screenshotFormat = screenshotFormat
    )

    captureScreenshot(composeRule, screenshotFile, screenshotQuality, rootMatcher)
  }

  /**
   * Takes a screenshot of the whole screen including any PopUp
   *
   * @param screenshotName is the name of the file that the screenshot will be saved under.
   * Usually, it's a pretty good idea to have this be pretty descriptive. By default, the name of
   * the screenshot will have the device and sdk information attached to it.
   *
   * @param screenshotNameSuffix is an optional param to add a suffix to the name of the screenshot file.
   *
   * @param screenshotDirectory allows you to specify where the screenshot taken should be saved in
   * the device. Note: On devices above API 29, saving directly to an external storage in not allowed.
   * It's recommended to pass in an app-specific path retrieved from the context e.g.
   * {@code context.filesDir} or {@code context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}.
   *
   * @param screenshotFormat specifies the format of the screenshot file.
   *
   * @param screenshotQuality specifies the level of compression of the screenshot.
   */
  @Synchronized
  fun takeScreenshot(
    screenshotName: String,
    screenshotNameSuffix: String = "",
    screenshotDirectory: String = defaultScreenshotDirectory,
    screenshotFormat: ScreenshotFormat = PNG,
    screenshotQuality: ScreenshotQuality = BEST,
  ) {
    val screenshotFile = getScreenshotFile(
      screenshotDirectory = screenshotDirectory,
      screenshotName = screenshotName,
      screenshotNameSuffix = screenshotNameSuffix,
      screenshotFormat = screenshotFormat
    )

    captureScreenshot(screenshotFile, screenshotQuality)
  }
}

private fun String.replaceWithUnderscore(): String {
  return lowercase(ENGLISH).replace(" ", "_")
}
