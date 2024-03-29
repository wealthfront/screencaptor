package com.wealthfront.screencaptor.viewmutator

import android.widget.TextView
import com.wealthfront.screencaptor.R

class TextViewMutator(content: CharSequence) : ViewMutator<TextView, CharSequence>(TextView::class.java, content) {
  override fun key(): Int = R.id.screencaptor_content_mutator

  override fun originalViewState(view: TextView): CharSequence = view.text

  override fun mutateView(view: TextView, value: CharSequence) {
    view.text = value
  }
}