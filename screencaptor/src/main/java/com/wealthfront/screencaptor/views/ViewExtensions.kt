package com.wealthfront.screencaptor.views

import android.content.res.Resources
import android.view.View
import android.view.ViewGroup

fun View.getAllChildren(): List<View> {
  val result = arrayListOf<View>()
  result.add(this)
  if (this is ViewGroup) {
    (0 until childCount).map { index ->
      val child = getChildAt(index)
      result.addAll(child.getAllChildren())
    }
  }
  return result
}

fun View.getViewResourceName(): String {
  return try {
    if (resources != null && id > 0) {
      resources.getResourceName(id)
    } else {
      "${javaClass.simpleName}: No id"
    }
  } catch (e: Resources.NotFoundException) {
    "Cannot find name for \${javaClass.simpleName}"
  }
}
