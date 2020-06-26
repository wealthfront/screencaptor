package com.wealthfront.screencaptor.views.modifier

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.lang.IllegalArgumentException

/**
 * This provides the ability to supply sample data for the screenshots and various implementation of the [DataModifier].
 */
interface ViewDataProcessor {
  fun modifyViews(view: View, viewDataModifiers: Set<DataModifier>): Set<DataModifier>
  fun resetViews(view: View, initialDataModifiers: Set<DataModifier>)
}

/**
 * Use this to provide different implementation for different custom views and then process the data in the [ViewDataProcessor].
 */
interface DataModifier {
  val id: Int
}

/**
 * Simple data modifier implementation for a text view.
 */
data class TextViewDataModifier(override val id: Int, val data: CharSequence) : DataModifier

/**
 * Simple data modifier implementation for an image view.
 */
data class ImageViewDataModifier(override val id: Int, val data: Drawable) : DataModifier
