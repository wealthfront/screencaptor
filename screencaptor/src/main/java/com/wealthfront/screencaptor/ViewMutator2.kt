package com.wealthfront.screencaptor

import android.view.View
import androidx.annotation.IdRes
import org.hamcrest.Matcher

data class ViewMutation<S : View, T>(val viewMatcher: Matcher<S>, val viewMutator2: ViewMutator2<S, T>, val desiredValue:)

abstract class ViewMutator2<S : View, T> {
  @IdRes
  protected abstract fun key(): Int

  protected abstract fun originalViewState(view: S): T

  fun mutate(view: S, value: T) {
    view.setTag(key(), originalViewState(view))
    mutateView(view, value)
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
}