package com.wealthfront.screencaptor.viewmutator

import android.view.View
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import com.wealthfront.screencaptor.ViewMutation

class ViewMutationImpl<S : View, T>(
  private val viewInteraction: ViewInteraction,
  private val viewMutator: ViewMutator<S, T>
) : ViewMutation {
  override fun getPerformAction(): ViewAction {
    return viewMutator.getMutatorAction()
  }

  override fun getRestoreAction(): ViewAction {
    return viewMutator.getRestorationAction()
  }

  override fun getViewInteraction(): ViewInteraction = viewInteraction
}