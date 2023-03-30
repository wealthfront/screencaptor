package com.wealthfront.screencaptor.views.mutator

import android.view.View
import android.widget.HorizontalScrollView
import android.widget.ListView
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import androidx.recyclerview.widget.RecyclerView

/**
 * Hides the scrollbar from showing up from any type of ScrollView.
 */
object ScrollbarHider : ViewMutator {

  override fun typesToMutate(): Set<Class<out View>> = setOf(
      ScrollView::class.java,
      HorizontalScrollView::class.java,
      NestedScrollView::class.java,
      ListView::class.java,
      RecyclerView::class.java
  )

  override fun mutateView(listOfViews: List<View>) {
    listOfViews.forEach {
      it.isVerticalScrollBarEnabled = false
      it.isHorizontalScrollBarEnabled = false
    }
  }
}
