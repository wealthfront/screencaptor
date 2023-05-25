package com.wealthfront.screencaptor.recyclerviewmutator

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher
import org.hamcrest.Matchers

abstract class RecyclerViewMutator<S : View, T>(private val viewClass: Class<S>, private val desiredValue: T) {
  fun mutate(view: S) {
    mutateView(view, desiredValue)
  }

  protected abstract fun mutateView(view: S, value: T)

  fun getMutatorAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "ViewAction to perform ${this@RecyclerViewMutator}"

      override fun getConstraints(): Matcher<View> = Matchers.instanceOf(viewClass)

      override fun perform(uiController: UiController, view: View) {
        this@RecyclerViewMutator.mutate(view as S)
        uiController.loopMainThreadUntilIdle()
      }
    }
  }
}