package com.wealthfront.screencaptor.recyclerviewmutator

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItem
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.wealthfront.screencaptor.ViewMutation
import org.hamcrest.Matcher

class RecyclerViewMutationOnItem<VH : RecyclerView.ViewHolder, S : View, T>(
  val recyclerViewMatcher: Matcher<View>,
  val recyclerViewItemMatcher: Matcher<View>,
  val viewMutator: RecyclerViewMutator<S, T>
) : ViewMutation {

  override fun getPerformInteraction(): ViewInteraction {
    return onView(recyclerViewMatcher)
  }

  override fun getRestoreInteraction(): ViewInteraction {
    return onView(recyclerViewMatcher)
  }

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