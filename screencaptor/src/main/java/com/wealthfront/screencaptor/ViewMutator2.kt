package com.wealthfront.screencaptor

import android.view.View
import androidx.annotation.IdRes

abstract class ViewMutator2<S, T> {
  @IdRes
  protected abstract fun key(): Int

  protected abstract fun originalViewState(view: View): S

  fun mutate(view: View, value: T) {
    view.setTag(key(), originalViewState(view))
    mutateView(view, value)
  }

  fun restore(view: View) {
    val state = storedViewState(view)
    restoreView(view, state)
  }

  protected abstract fun mutateView(view: View, value: T)

  protected abstract fun restoreView(view: View, value: S)

  @Suppress("UNCHECKED_CAST")
  protected final fun storedViewState(view: View): S {
    return view.getTag(key()) as S
  }
}