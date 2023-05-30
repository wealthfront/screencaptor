package com.wealthfront.screencaptor.viewmutator

import android.view.View
import androidx.annotation.IdRes
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher

abstract class ViewMutator<S : View, T>(private val viewClass: Class<S>, private val desiredValue: T) {
  @IdRes
  protected abstract fun key(): Int

  protected abstract fun originalViewState(view: S): T

  fun mutate(view: S) {
    view.setTag(key(), originalViewState(view))
    mutateView(view, desiredValue)
  }

  fun restore(view: S) {
    val state = storedViewState(view)
    mutateView(view, state)
    view.setTag(key(), null)
  }

  protected abstract fun mutateView(view: S, value: T)

  @Suppress("UNCHECKED_CAST")
  private fun storedViewState(view: S): T {
    return view.getTag(key()) as T
  }

  fun getMutatorAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "ViewAction to perform ${this@ViewMutator}"

      override fun getConstraints(): Matcher<View> = isAssignableFrom(viewClass)

      override fun perform(uiController: UiController, view: View) {
        this@ViewMutator.mutate(view as S)
        uiController.loopMainThreadUntilIdle()
      }
    }
  }

  fun getRestorationAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "ViewAction to undo ${this@ViewMutator}"

      override fun getConstraints(): Matcher<View> = isAssignableFrom(viewClass)

      override fun perform(uiController: UiController, view: View) {
        this@ViewMutator.restore(view as S)
        uiController.loopMainThreadUntilIdle()
      }
    }
  }
}