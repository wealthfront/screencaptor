package com.wealthfront.screencaptor.views.modifier

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import java.lang.IllegalArgumentException

internal class DefaultViewDataProcessor : ViewDataProcessor {

  override fun modifyViews(view: View, viewDataModifiers: Set<DataModifier>): Set<DataModifier> {
    val listOfInitialStateModifier = mutableSetOf<DataModifier>()
    viewDataModifiers.forEach { modifier ->
      val initialStateModifier: DataModifier
      when (modifier) {
        is TextViewDataModifier -> {
          val textView = view.findViewById<TextView>(modifier.id)
          textView?.let {
            initialStateModifier = TextViewDataModifier(modifier.id, textView.text)
            textView.text = modifier.data
            listOfInitialStateModifier.add(initialStateModifier)
          }
        }
        is ImageViewDataModifier -> {
          val imageView = view.findViewById<ImageView>(modifier.id)
          imageView?.let {
            initialStateModifier = ImageViewDataModifier(modifier.id, imageView.drawable)
            imageView.setImageDrawable(modifier.data)
            listOfInitialStateModifier.add(initialStateModifier)
          }
        }
        else -> throw IllegalArgumentException("Unhandled modifier: ${modifier::class}. " +
            "Please provide a custom ViewDataProcessor if you want to handle this modifier")
      }
    }
    return listOfInitialStateModifier
  }

  override fun resetViews(view: View, initialDataModifiers: Set<DataModifier>) {
    initialDataModifiers.forEach { modifier ->
      when (modifier) {
        is TextViewDataModifier -> view.findViewById<TextView>(modifier.id).text = modifier.data
        is ImageViewDataModifier -> view.findViewById<ImageView>(modifier.id).setImageDrawable(modifier.data)
      }
    }
  }
}