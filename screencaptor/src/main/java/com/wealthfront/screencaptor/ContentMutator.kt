package com.wealthfront.screencaptor

import android.widget.TextView

class ContentMutator(content: CharSequence): ViewMutator2<TextView, CharSequence>(TextView::class.java, content) {
  override fun key(): Int = R.id.content_mutator

  override fun originalViewState(view: TextView): CharSequence = view.text

  override fun restoreView(view: TextView, value: CharSequence) {
    view.text = value
  }

  override fun mutateView(view: TextView, value: CharSequence) {
    view.text = value
  }
}