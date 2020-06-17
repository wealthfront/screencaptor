package com.wealthfront.screencaptor.views.modifier

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable

fun <T : Drawable> T.pixelsEqualTo(t: T?) = toBitmap().pixelsEqualTo(t?.toBitmap(), true)

fun Bitmap.pixelsEqualTo(otherBitmap: Bitmap?, shouldRecycle: Boolean = false) = otherBitmap?.let { other ->
  if (width == other.width && height == other.height) {
    val res = toPixels().contentEquals(other.toPixels())
    if (shouldRecycle) {
      doRecycle().also { otherBitmap.doRecycle() }
    }
    res
  } else false
} ?: kotlin.run { false }

fun Bitmap.doRecycle() {
  if (!isRecycled) recycle()
}

fun <T : Drawable> T.toBitmap(): Bitmap {
  if (this is BitmapDrawable) return bitmap

  val drawable: Drawable = this
  val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
  val canvas = Canvas(bitmap)
  drawable.setBounds(0, 0, canvas.width, canvas.height)
  drawable.draw(canvas)
  return bitmap
}

fun Bitmap.toPixels() = IntArray(width * height).apply { getPixels(this, 0, width, 0, 0, width, height) }
