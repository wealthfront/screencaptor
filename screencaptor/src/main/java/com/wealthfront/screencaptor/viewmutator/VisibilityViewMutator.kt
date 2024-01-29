package com.wealthfront.screencaptor.viewmutator

import android.view.View
import com.wealthfront.screencaptor.R

class VisibilityViewMutator<S : View>(classType: Class<S>,visibility: Int) : ViewMutator<S, Int>(classType, visibility) {
  override fun key(): Int = R.id.screencaptor_visibility_mutator

  override fun originalViewState(view: S): Int = view.visibility

  override fun mutateView(view: S, value: Int) {
    view.visibility = value
  }
}