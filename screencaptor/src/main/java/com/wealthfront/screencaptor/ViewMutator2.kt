package com.wealthfront.screencaptor

import android.view.View
import androidx.annotation.IdRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import org.hamcrest.Matcher
import org.hamcrest.Matchers

interface ViewMutation {
  fun getPerformInteraction(): ViewInteraction
  fun getRestoreInteraction(): ViewInteraction
  fun getPerformAction(): ViewAction
  fun getRestoreAction(): ViewAction
}

class ViewMutationImpl<S : View, T>(
  val viewMatcher: Matcher<View>,
  val viewMutator: ViewMutator2<S, T>,
  val viewInteractionModifier: (ViewInteraction) -> ViewInteraction = { it }
) : ViewMutation {
  override fun getPerformAction(): ViewAction {
    return viewMutator.getMutatorAction()
  }

  override fun getRestoreAction(): ViewAction {
    return viewMutator.getRestorationAction()
  }

  override fun getPerformInteraction(): ViewInteraction {
    return viewInteractionModifier(onView(viewMatcher))
  }

  override fun getRestoreInteraction(): ViewInteraction {
    return viewInteractionModifier(onView(viewMutator.getRestorationViewMatcher()))
  }
}

class RecyclerViewMutationOnItem<VH : ViewHolder, S : View, T>(
  val recyclerViewMatcher: Matcher<View>,
  val recyclerViewItemMatcher: Matcher<View>,
  val viewMutator: ViewMutator2<S, T>
) : ViewMutation {

  override fun getPerformInteraction(): ViewInteraction {
    return onView(recyclerViewMatcher)
  }

  override fun getRestoreInteraction(): ViewInteraction {
    return onView(recyclerViewMatcher)
  }

  override fun getPerformAction(): ViewAction {
    return RecyclerViewActions.actionOnItem<VH>(recyclerViewItemMatcher, viewMutator.getMutatorAction())
  }

  override fun getRestoreAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "Recreate views by calling notifyDataSetChanged"

      override fun getConstraints(): Matcher<View> = isAssignableFrom(RecyclerView::class.java)

      override fun perform(uiController: UiController, view: View) {
        (view as RecyclerView).adapter!!.notifyDataSetChanged()
      }
    }
  }
}

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
    view.setTag(key(), null)
  }

  protected abstract fun mutateView(view: S, value: T)

  protected abstract fun restoreView(view: S, value: T)

  @Suppress("UNCHECKED_CAST")
  protected final fun storedViewState(view: S): T {
    return view.getTag(key()) as T
  }

  fun getRestorationViewMatcher(): Matcher<View> {
    return ViewMatchers.withTagKey(key())
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