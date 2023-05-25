package com.wealthfront.screencaptor.recyclerviewmutator

import android.widget.TextView

class RecyclerViewTextMutator(content: CharSequence) : RecyclerViewMutator<TextView, CharSequence>(TextView::class.java, content) {
  override fun mutateView(view: TextView, value: CharSequence) {
    view.text = value
  }
}