package com.wealthfront.screencaptor.views.modifier

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView

internal class DefaultViewDataProcessor : ViewDataProcessor {

  override fun modifyViews(view: View, viewDataModifiers: List<DataModifier>): List<DataModifier> {
    val listOfInitialStateModifier = mutableListOf<DataModifier>()
    viewDataModifiers.forEach { modifier ->
      val initialStateModifier: DataModifier
      when (modifier) {
        is TextViewDataModifier -> {
          val textView = view.findViewById<TextView>(modifier.id)
          initialStateModifier = TextViewDataModifier(modifier.id, textView.text)
          textView.text = modifier.data
          listOfInitialStateModifier.add(initialStateModifier)
        }
        is ImageViewDataModifier -> {
          val imageView = view.findViewById<ImageView>(modifier.id)
          initialStateModifier = ImageViewDataModifier(modifier.id, imageView.drawable)
          imageView.setImageDrawable(modifier.data)
          listOfInitialStateModifier.add(initialStateModifier)
        }
      }
    }
    return listOfInitialStateModifier
  }

  override fun resetViews(view: View, initialDataModifiers: List<DataModifier>) {
    initialDataModifiers.forEach { modifier ->
      when (modifier) {
        is TextViewDataModifier -> view.findViewById<TextView>(modifier.id).text = modifier.data
        is ImageViewDataModifier -> view.findViewById<ImageView>(modifier.id).setImageDrawable(modifier.data)
      }
    }
  }
}

interface ViewDataProcessor {
  fun modifyViews(view: View, viewDataModifiers: List<DataModifier>): List<DataModifier>
  fun resetViews(view: View, initialDataModifiers: List<DataModifier>)
}

interface DataModifier {
  val id: Int
}

data class TextViewDataModifier(override val id: Int, val data: CharSequence) : DataModifier
data class ImageViewDataModifier(override val id: Int, val data: Drawable) : DataModifier
