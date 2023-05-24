package com.wealthfront.screencaptor

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers

inline fun <reified S: View, T> getMutatorAction(mutator: ViewMutator2<S, T>, desiredValue: T): ViewAction {
  return object : ViewAction {
    override fun getDescription(): String = "ViewAction to perform $mutator"

    override fun getConstraints(): Matcher<View> = Matchers.instanceOf(S::class.java)

    override fun perform(uiController: UiController, view: View) {
      mutator.mutate(view as S, desiredValue)
      uiController.loopMainThreadUntilIdle()
    }
  }
}

inline fun <reified S: View, T> getRestorationAction(mutator: ViewMutator2<S, T>): ViewAction {
  return object : ViewAction {
    override fun getDescription(): String = "ViewAction to perform $mutator"

    override fun getConstraints(): Matcher<View> = Matchers.instanceOf(S::class.java)

    override fun perform(uiController: UiController, view: View) {
      mutator.restore(view as S)
      uiController.loopMainThreadUntilIdle()
    }
  }
}