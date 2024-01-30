package com.wealthfront.screencaptor.sample.impl

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.Description

object RecyclerViewMatchers {

  fun hasItemWithText(text: String) = object : BoundedMatcher<View, View>(View::class.java) {
    override fun describeTo(description: Description) {
      description.appendText("Searching for item with: $text")
    }

    override fun matchesSafely(item: View): Boolean {
      if (item is RecyclerView) {
        for (i in 0 until item.childCount) {
          val childView = item.findViewHolderForAdapterPosition(i)!!.itemView
          if (ViewMatchers.withText(text).matches(childView)) {
            return true
          }
        }
      }
      return false
    }
  }
}