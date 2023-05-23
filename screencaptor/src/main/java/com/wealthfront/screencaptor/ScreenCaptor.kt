package com.wealthfront.screencaptor

import android.app.Activity
import android.graphics.Bitmap
import android.os.Build.MANUFACTURER
import android.os.Build.MODEL
import android.os.Build.VERSION.SDK_INT
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import com.wealthfront.screencaptor.ScreenCaptor.takeScreenshot
import com.wealthfront.screencaptor.ScreenshotFormat.PNG
import com.wealthfront.screencaptor.ScreenshotQuality.BEST
import com.wealthfront.screencaptor.views.modifier.DataModifier
import com.wealthfront.screencaptor.views.modifier.DefaultViewDataProcessor
import com.wealthfront.screencaptor.views.modifier.ViewDataProcessor
import com.wealthfront.screencaptor.views.modifier.ViewVisibilityModifier
import com.wealthfront.screencaptor.views.mutator.CursorHider
import com.wealthfront.screencaptor.views.mutator.ScrollbarHider
import com.wealthfront.screencaptor.views.mutator.ViewMutator
import com.wealthfront.screencaptor.views.mutator.ViewTreeMutator
import eu.bolt.screenshotty.Screenshot
import eu.bolt.screenshotty.ScreenshotActionOrder
import eu.bolt.screenshotty.ScreenshotManagerBuilder
import eu.bolt.screenshotty.util.ScreenshotFileSaver
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.Matchers.any
import java.io.File
import java.util.Locale.ENGLISH

/**
 * Has the ability to take a screenshot of the current view displayed on the screen using the method [takeScreenshot].
 */
object ScreenCaptor {

  data class ViewTreeState(val viewVisibilityStates: Map<Int, Int>, val viewDataStates: Set<DataModifier>)

  private val mainHandler = Handler(Looper.getMainLooper())
  private val SCREENSHOT = javaClass.simpleName
  private const val defaultScreenshotDirectory = "screenshots"

  private fun captureScreenshot(
    activityScenario: ActivityScenario<out AppCompatActivity>,
    screenshotName: String,
    screenshotNameSuffix: String = "",
    screenshotDirectory: String = defaultScreenshotDirectory,
    screenshotFormat: ScreenshotFormat = PNG,
    screenshotQuality: ScreenshotQuality = BEST,
    onSuccess: (Screenshot) -> Unit
  ) {
    if (!File(screenshotDirectory).exists()) {
      Log.d(SCREENSHOT, "Creating directory $screenshotDirectory since it does not exist")
      val screenshotDirsCreated = File(screenshotDirectory).mkdirs()
      assert(screenshotDirsCreated)
    }

    val deviceName = MANUFACTURER.replaceWithUnderscore() + "_" + MODEL.replaceWithUnderscore()
    val screenshotId =
      "${screenshotName.toLowerCase(ENGLISH)}_${deviceName}_${SDK_INT}_$screenshotNameSuffix"
    val screenshotFile = File("$screenshotDirectory/$screenshotId.${screenshotFormat.extension}")

    activityScenario.onActivity { activity ->
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
  }

  /**
   * Takes a screenshot whenever the method is called from the test thread or the main thread.
   * Also note that the operation takes place entirely on the main thread in a synchronized fashion.
   * In this method, we post the operation to the main thread since we mutate the views and change
   * the visibility of certain views before and after taking the screenshot.
   *
   * @param activity to be captured as the screenshot and saved on the path provided.
   *
   * @param screenshotName is the name of the file that the screenshot will be saved under.
   * Usually, it's a pretty good idea to have this be pretty descriptive. By default, the name of
   * the screenshot will have the device and sdk information attached to it.
   *
   * @param screenshotNameSuffix is an optional param to add a suffix to the name of the screenshot file.
   *
   * @param viewIdsToExclude takes in set of ids to exclude from the screenshot by changing the
   * visibility to be invisible when the screenshot is taken and then turning it back to the
   * visibility that the view initially had.
   *
   * @param viewModifiers takes in a set of data modifiers which will be processed by the [viewDataProcessor].
   *
   * @param viewDataProcessor allows you to pass in a custom view modification processor.
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
    activityScenario: ActivityScenario<out AppCompatActivity>,
    screenshotName: String,
    screenshotNameSuffix: String = "",
    viewIdsToExclude: Set<Int> = setOf(),
    viewModifiers: Set<DataModifier> = setOf(),
    viewDataProcessor: ViewDataProcessor = DefaultViewDataProcessor(),
    viewMutators: Set<ViewMutator> = setOf(ScrollbarHider, CursorHider),
    screenshotDirectory: String = defaultScreenshotDirectory,
    screenshotFormat: ScreenshotFormat = PNG,
    screenshotQuality: ScreenshotQuality = BEST
  ) {

    val mutator = VisibilityMutator()
    runViewMutationInteraction(viewIdsToExclude, mutator, View.INVISIBLE).perform()
    val idlingResource = ScreenshotIdlingResource()

    IdlingRegistry.getInstance().register(idlingResource)
    captureScreenshot(
      activityScenario,
      screenshotName,
      screenshotNameSuffix,
      screenshotDirectory,
      screenshotFormat,
      screenshotQuality
    ) { screenshot ->
      idlingResource.setScreenshotCaptured()
      IdlingRegistry.getInstance().unregister(idlingResource)
    }

    runViewRestorationInteraction(viewIdsToExclude, mutator)
  }

  private fun <S, T> runViewMutationInteraction(viewIds: Set<Int>, mutator: ViewMutator2<S, T>, desiredValue: T): ViewInteraction {
    val viewAction = object : ViewAction {
      override fun getDescription(): String = "Wrapper for ViewMutator2"

      override fun getConstraints(): Matcher<View> = Matchers.instanceOf(View::class.java)

      override fun perform(uiController: UiController, view: View) {
        mutator.mutate(view, desiredValue)
      }
    }
    val viewMatchers = viewIds.map {
      withId(it)
    }
    return Espresso.onView(Matchers.anyOf(viewMatchers)).perform(viewAction)
  }

  private fun <S, T> runViewRestorationInteraction(viewIds: Set<Int>, mutator: ViewMutator2<S, T>): ViewInteraction {
    val viewAction = object : ViewAction {
      override fun getDescription(): String = "Wrapper for ViewMutator2"

      override fun getConstraints(): Matcher<View> = Matchers.instanceOf(View::class.java)

      override fun perform(uiController: UiController, view: View) {
        mutator.restore(view)
      }
    }
    val viewMatchers = viewIds.map {
      withId(it)
    }
    return Espresso.onView(Matchers.anyOf(viewMatchers))
      //.inRoot(RootMatchers.)
      .perform(viewAction)
  }

  private fun modifyViewBeforeScreenshot(
    view: View,
    viewDataProcessor: ViewDataProcessor,
    viewMutators: Set<ViewMutator>,
    viewModifiers: Set<DataModifier>,
    viewIdsToExclude: Set<Int>
  ): ViewTreeState {
    Log.d(SCREENSHOT, "Mutating views as needed inside $view")
    ViewTreeMutator.Builder()
      .viewMutators(viewMutators)
      .mutateView(view)
      .mutate()

    Log.d(SCREENSHOT, "Disabling views: $viewIdsToExclude")
    val initialVisibilityOfViews = ViewVisibilityModifier.hideViews(view, viewIdsToExclude)
    val initialDataOfViews = viewDataProcessor.modifyViews(view, viewModifiers)
    return ViewTreeState(initialVisibilityOfViews, initialDataOfViews)
  }

  private fun resetViewTreeAfterScreenshot(
    viewDataProcessor: ViewDataProcessor,
    view: View,
    initialViewStates: Map<View, ViewTreeState>,
    viewIdsToExclude: Set<Int>
  ) {
    Log.d(SCREENSHOT, "Enabling views: $viewIdsToExclude")
    val initialViewState = initialViewStates[view]!!
    ViewVisibilityModifier.showViews(view, viewIdsToExclude, initialViewState.viewVisibilityStates)
    viewDataProcessor.resetViews(view, initialViewState.viewDataStates)
  }

  private fun getRootViewsFromActivity(activity: Activity): List<View> {
    val windowMgr = activity.windowManager!!
    val globalWindowManagerField = windowMgr.javaClass.getDeclaredField("mGlobal")
      .apply { isAccessible = true }
    val globalWindowManager = globalWindowManagerField.get(windowMgr)

    val viewsField = globalWindowManager.javaClass.getDeclaredField("mViews")
      .apply { isAccessible = true }
    @Suppress("UNCHECKED_CAST")
    return viewsField.get(globalWindowManager) as List<View>
  }
}

private fun String.replaceWithUnderscore(): String {
  return toLowerCase(ENGLISH).replace(" ", "_")
}
