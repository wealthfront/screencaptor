package com.wealthfront.screencaptor.viewmutator

import android.view.View
import com.wealthfront.screencaptor.R

class VisibilityMutator(visibility: Int) : ViewMutator<View, Int>(View::class.java, visibility) {
  override fun key(): Int = R.id.visibility_mutator

  override fun originalViewState(view: View): Int = view.visibility

  override fun mutateView(view: View, value: Int) {
    view.visibility = value
  }

  override fun restoreView(view: View, value: Int) {
    view.visibility = value
  }
}