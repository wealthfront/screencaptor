package com.wealthfront.screencaptor.viewmutator

import android.view.View
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import com.wealthfront.screencaptor.ViewMutation
import org.hamcrest.Matcher

class ViewMutationImpl<S : View, T>(
  val viewMatcher: Matcher<View>,
  val viewMutator: ViewMutator<S, T>,
  val viewInteractionModifier: (ViewInteraction) -> ViewInteraction = { it }
) : ViewMutation {
  override fun getPerformAction(): ViewAction {
    return viewMutator.getMutatorAction()
  }

  override fun getRestoreAction(): ViewAction {
    return viewMutator.getRestorationAction()
  }

  override fun getPerformInteraction(): ViewInteraction {
    return viewInteractionModifier(Espresso.onView(viewMatcher))
  }

  override fun getRestoreInteraction(): ViewInteraction {
    return viewInteractionModifier(Espresso.onView(viewMutator.getRestorationViewMatcher()))
  }
}