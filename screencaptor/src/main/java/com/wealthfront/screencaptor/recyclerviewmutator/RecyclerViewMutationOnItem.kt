package com.wealthfront.screencaptor.recyclerviewmutator

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.wealthfront.screencaptor.ViewMutation
import org.hamcrest.Matcher

class RecyclerViewMutationOnItem<VH : RecyclerView.ViewHolder, S : View, T>(
  private val recyclerViewInteraction: ViewInteraction,
  private val recyclerViewItemMatcher: Matcher<View>,
  private val viewMutator: RecyclerViewMutator<S, T>
) : ViewMutation {

  override fun getViewInteraction(): ViewInteraction = recyclerViewInteraction

  override fun getPerformAction(): ViewAction {
    return actionOnItem<VH>(recyclerViewItemMatcher, viewMutator.getMutatorAction())
  }

  override fun getRestoreAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "Recreate RecyclerView item views"

      override fun getConstraints(): Matcher<View> = isAssignableFrom(RecyclerView::class.java)

      override fun perform(uiController: UiController, view: View) {
        (view as RecyclerView).adapter!!.notifyDataSetChanged()
      }
    }
  }
}

class RecyclerViewMutationOnPosition<VH : RecyclerView.ViewHolder, S : View, T>(
  private val recyclerViewInteraction: ViewInteraction,
  private val position: Int,
  private val viewMutator: RecyclerViewMutator<S, T>
) : ViewMutation {
  override fun getViewInteraction(): ViewInteraction = recyclerViewInteraction

  override fun getPerformAction(): ViewAction {
    return RecyclerViewActions.actionOnItemAtPosition<VH>(position, viewMutator.getMutatorAction())
  }

  override fun getRestoreAction(): ViewAction {
    return object : ViewAction {
      override fun getDescription(): String = "Recreate RecyclerView item views"

      override fun getConstraints(): Matcher<View> = isAssignableFrom(RecyclerView::class.java)

      override fun perform(uiController: UiController, view: View) {
        (view as RecyclerView).adapter!!.notifyDataSetChanged()
      }
    }
  }
}