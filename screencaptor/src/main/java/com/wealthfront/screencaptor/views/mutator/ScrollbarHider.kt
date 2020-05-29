package com.wealthfront.screencaptor.views.mutator

import android.view.View
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView

/**
 * Hides the scrollbar from showing up from any type of ScrollView.
 */
object ScrollbarHider : ViewMutator {

  override fun typesToMutate(): Set<Class<out View>> = setOf(ScrollView::class.java, NestedScrollView::class.java)

  override fun mutateView(listOfViews: List<View>) {
    listOfViews.forEach {
      it.isVerticalScrollBarEnabled = false
      it.isHorizontalScrollBarEnabled = false
    }
  }
}
