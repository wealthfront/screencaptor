package com.wealthfront.screencaptor.views.modifier

import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import com.wealthfront.screencaptor.views.extensions.getAllChildren

internal object ViewVisibilityModifier {

  internal fun hideViews(
    view: View,
    viewIdsToExclude: Set<Int>
  ): Map<Int, Int> {
    val visibilityState = mutableMapOf<Int, Int>()
    view.getAllChildren().forEach { child ->
      if (viewIdsToExclude.any { child.id == it }) {
        visibilityState[child.id] = child.visibility
        if (child.visibility == VISIBLE) {
          child.visibility = INVISIBLE
        }
      }
    }
    return visibilityState
  }

  internal fun showViews(
    view: View,
    viewIdsToExclude: Set<Int>,
    initialStateOfViews: Map<Int, Int>
  ) {
    view.getAllChildren().forEach { child ->
      if (viewIdsToExclude.any { child.id == it }) {
        child.visibility = initialStateOfViews.getValue(child.id)
      }
    }
  }
}
