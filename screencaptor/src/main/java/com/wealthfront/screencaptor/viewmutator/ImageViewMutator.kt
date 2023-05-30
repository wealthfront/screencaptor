package com.wealthfront.screencaptor.viewmutator

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.wealthfront.screencaptor.R

class ImageViewMutator(drawable: Drawable) : ViewMutator<ImageView, Drawable>(ImageView::class.java, drawable) {
  override fun key(): Int = R.id.image_mutator

  override fun mutateView(view: ImageView, value: Drawable) {
    view.setImageDrawable(value)
  }

  override fun originalViewState(view: ImageView): Drawable = view.drawable
}