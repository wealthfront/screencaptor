package com.wealthfront.screencaptor

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import org.hamcrest.Matcher
import org.hamcrest.Matchers

data class ViewMutation<S : View, T>(
  val viewInteraction: ViewInteraction,
  val viewMutator: ViewMutator2<S, T>
)

abstract class ViewMutator2<S : View, T>(private val viewClass: Class<S>, private val desiredValue: T) {
  @IdRes
  protected abstract fun key(): Int

  protected abstract fun originalViewState(view: S): T

  fun mutate(view: S) {
    view.setTag(key(), originalViewState(view))
    mutateView(view, desiredValue)
  }

  fun restore(view: S) {
    val state = storedViewState(view)
    restoreView(view, state)
  }

  protected abstract fun mutateView(view: S, value: T)

  protected abstract fun restoreView(view: S, value: T)

  @Suppress("UNCHECKED_CAST")
  protected final fun storedViewState(view: S): T {
    return view.getTag(key()) as T
  }

  fun getMutatorAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "ViewAction to perform ${this@ViewMutator2}"

      override fun getConstraints(): Matcher<View> = Matchers.instanceOf(viewClass)

      override fun perform(uiController: UiController, view: View) {
        this@ViewMutator2.mutate(view as S)
        uiController.loopMainThreadUntilIdle()
      }
    }
  }

  fun getRestorationAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "ViewAction to undo ${this@ViewMutator2}"

      override fun getConstraints(): Matcher<View> = Matchers.instanceOf(viewClass)

      override fun perform(uiController: UiController, view: View) {
        this@ViewMutator2.restore(view as S)
        uiController.loopMainThreadUntilIdle()
      }
    }
  }
}