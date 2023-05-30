package com.wealthfront.screencaptor.viewmutator

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import com.wealthfront.screencaptor.ViewMutation
import org.hamcrest.Matcher

class ViewMutationImpl<S : View, T>(
  private val viewInteraction: ViewInteraction,
  private val viewMutator: ViewMutator<S, T>
) : ViewMutation {

  constructor(
    viewMatcher: Matcher<View>, viewMutator: ViewMutator<S, T>
  ) : this(onView(viewMatcher), viewMutator)

  override fun getPerformAction(): ViewAction {
    return viewMutator.getMutatorAction()
  }

  override fun getRestoreAction(): ViewAction {
    return viewMutator.getRestorationAction()
  }

  override fun getViewInteraction(): ViewInteraction = viewInteraction
}