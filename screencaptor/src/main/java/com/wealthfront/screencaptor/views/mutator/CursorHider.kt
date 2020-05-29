package com.wealthfront.screencaptor.views.mutator

import android.view.View
import android.widget.TextView

/**
 * Hides the cursor from showing up from any type of TextView (EditText,SelectableEditText, etc).
 */
object CursorHider : ViewMutator {

  override fun typesToMutate() = setOf(TextView::class.java)

  override fun mutateView(listOfViews: List<View>) {
    listOfViews.forEach { view ->
      (view as TextView).isCursorVisible = false
    }
  }
}
