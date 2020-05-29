package com.wealthfront.screencaptor.views.mutator

import android.view.View

/**
 * Implementing this interface allows you to create a custom plugin to modify the behaviour of a
 * class of views like for example [ScrollbarHider] allows you to hide the scrollbars and [CursorHider]
 * allows you to hide the cursor from showing up on the screenshot. This dramatically increases the
 * stability and repeatability of the screenshots.
 */
interface ViewMutator {

  /**
   * Specify the class of views that you want to filter to listen inside [mutateView]
   */
  fun typesToMutate(): Set<Class<out View>>

  /**
   * This method gives you the list of views that you specified in the [typesToMutate] method.
   * You may cast the views as needed and change the behaviour as you may seem fit.
   */
  fun mutateView(listOfViews: List<View>)
}
