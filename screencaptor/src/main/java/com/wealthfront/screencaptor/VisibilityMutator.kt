package com.wealthfront.screencaptor

import android.view.View

class VisibilityMutator(visibility: Int) : ViewMutator2<View, Int>(View::class.java, visibility) {
  override fun key(): Int = R.id.visibility_mutator

  override fun originalViewState(view: View): Int = view.visibility

  override fun mutateView(view: View, value: Int) {
    view.visibility = value
  }

  override fun restoreView(view: View, value: Int) {
    view.visibility = value
  }
}