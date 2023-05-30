package com.wealthfront.screencaptor.globalmutator

import android.view.View
import android.widget.TextView
import com.wealthfront.screencaptor.GlobalViewMutation

/**
 * Hides the cursor from showing up from any type of TextView (EditText,SelectableEditText, etc).
 */
object CursorHider : GlobalViewMutation {

  override fun typesToMutate() = setOf(TextView::class.java)

  override fun mutateView(listOfViews: List<View>) {
    listOfViews.forEach { view ->
      (view as TextView).isCursorVisible = false
    }
  }
}
