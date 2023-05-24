package com.wealthfront.screencaptor

import android.view.View
import androidx.annotation.IdRes

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