package com.wealthfront.screencaptor

import android.graphics.Bitmap
import android.graphics.Bitmap.Config.ARGB_8888
import android.graphics.Canvas
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Build.VERSION.SDK_INT
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.wealthfront.screencaptor.ScreenshotFormat.PNG
import com.wealthfront.screencaptor.ScreenshotQuality.BEST
import com.wealthfront.screencaptor.views.modifier.DataModifier
import com.wealthfront.screencaptor.views.modifier.DefaultViewDataProcessor
import com.wealthfront.screencaptor.views.modifier.ViewDataProcessor
import com.wealthfront.screencaptor.views.mutator.ViewTreeMutator
import com.wealthfront.screencaptor.views.modifier.ViewVisibilityModifier
import com.wealthfront.screencaptor.views.mutator.ScrollbarHider
import com.wealthfront.screencaptor.views.mutator.CursorHider
import com.wealthfront.screencaptor.views.mutator.ViewMutator
import java.io.File
import java.io.FileOutputStream
import java.util.Locale.ENGLISH

/**
 * Has the ability to take a screenshot of the current view displayed on the screen using the method [takeScreenshot].
 */
object ScreenCaptor {

  private val mainHandler = Handler(Looper.getMainLooper())
  private val SCREENSHOT = javaClass.simpleName

  /**
   * Takes a screenshot whenever the method is called from the test thread or the main thread.
   * Also note that the operation takes place entirely on the main thread in a synchronized fashion.
   * In this method, we post the operation to the main thread since we mutate the views and change
   * the visibility of certain views before and after taking the screenshot. 
   *
   * @param view to be captured as the screenshot and saved on the path provided.
   *
   * @param screenshotFilename is the name of the file that the screenshot will be saved under.
   * Usually, it's a pretty good idea to have this be pretty descriptive. By default, the name of
   * the screenshot will have the device and sdk information attached to it.
   *
   * @param screenshotNameSuffix is an optional param to add a suffix to the name of the screenshot file.
   *
   * @param viewIdsToExclude takes in set of ids to exclude from the screenshot by changing the
   * visibility to be invisible when the screenshot is taken and then turning it back to the
   * visibility that the view initially had.
   *
   * @param viewModifiers takes in a set of data modifiers which will be processed by the [dataProcessor].
   *
   * @param dataProcessor allows you to present a custom modification processor.
   *
   * @param viewMutators allows you to mutate the subclasses of views in any particular way
   * (Hides scrollbars and cursors by default).
   *
   * @param screenshotDirectory allows you to specify where the screenshot taken should be saved in
   * the device. Note: On devices above API 29, saving directly to an external storage in not allowed.
   * So remember to pass in a valid path retrieved from the context as follows
   * {@code context.filesDir or context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)}.
   *
   * @param screenshotFormat specifies the format of the screenshot file.
   *
   * @param screenshotQuality specifies the level of compression of the screenshot.
   *
   */
  @Synchronized
  fun takeScreenshot(
    view: View,
    screenshotFilename: String,
    screenshotNameSuffix: String = "",
    viewIdsToExclude: Set<Int> = setOf(),
    viewModifiers: Set<DataModifier> = setOf(),
    dataProcessor: ViewDataProcessor = DefaultViewDataProcessor(),
    viewMutators: Set<ViewMutator> = setOf(ScrollbarHider, CursorHider),
    screenshotDirectory: String = "/sdcard/screenshots",
    screenshotFormat: ScreenshotFormat = PNG,
    screenshotQuality: ScreenshotQuality = BEST
  ) {

    val device = MANUFACTURER.replaceWithUnderscore() + "_" + MODEL.replaceWithUnderscore()

    val screenshotName = "${screenshotFilename.toLowerCase(ENGLISH)}_${device}_${SDK_INT}_$screenshotNameSuffix"
    val screenshotFile = "$screenshotDirectory/$screenshotName.${screenshotFormat.extension}"

    Log.d(SCREENSHOT, "Posting to main thread for '$screenshotFile'")
    mainHandler.post {
      Log.d(SCREENSHOT, "Root view has height(${view.height}) and width(${view.width})")
      if (view.width == 0 || view.height == 0) {
        throw IllegalStateException("This view ($view) has no height or width. Is ${view.id} the currently displayed activity?")
      }

      Log.d(SCREENSHOT, "Mutating views as needed!")
      ViewTreeMutator.Builder()
        .viewMutators(viewMutators)
        .mutateView(view)
        .mutate()

      Log.d(SCREENSHOT, "Disabling views: $viewIdsToExclude")
      val initialStateOfViews = ViewVisibilityModifier.hideViews(view, viewIdsToExclude)
      val initialDataOfViews = dataProcessor.modifyViews(view, viewModifiers)
      view.requestLayout()

      Log.d(SCREENSHOT, "Taking screenshot for '$screenshotFile'")
      val bitmap = Bitmap.createBitmap(view.width, view.height, ARGB_8888)
      val canvas = Canvas(bitmap)
      view.draw(canvas)

      if (!File(screenshotDirectory).exists()) {
        Log.d(SCREENSHOT, "Creating directory $screenshotDirectory since it does not exist")
        File(screenshotDirectory).mkdirs()
      }

      Log.d(SCREENSHOT, "Writing to disk for '$screenshotFile'")
      bitmap.compress(screenshotFormat.compression, screenshotQuality.value, FileOutputStream(screenshotFile))
      Log.d(SCREENSHOT, "Successfully wrote to disk for '$screenshotFile'")

      Log.d(SCREENSHOT, "Enabling views: $viewIdsToExclude")
      ViewVisibilityModifier.showViews(view, viewIdsToExclude, initialStateOfViews)
      dataProcessor.resetViews(view, initialDataOfViews)
    }
  }
}

private fun String.replaceWithUnderscore(): String {
  return toLowerCase(ENGLISH).replace(" ", "_")
}
