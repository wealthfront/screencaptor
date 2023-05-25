package com.wealthfront.screencaptor.viewmutator

import android.widget.TextView
import com.wealthfront.screencaptor.R

class CursorMutator: ViewMutator<TextView, Boolean>(TextView::class.java, false) {
  override fun key(): Int = R.id.cursor_mutator

  override fun originalViewState(view: TextView): Boolean = view.isCursorVisible

  override fun restoreView(view: TextView, value: Boolean) {
    view.isCursorVisible = value
  }

  override fun mutateView(view: TextView, value: Boolean) {
    view.isCursorVisible = value
  }
}