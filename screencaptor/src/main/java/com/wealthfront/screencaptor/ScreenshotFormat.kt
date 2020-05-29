package com.wealthfront.screencaptor

import android.graphics.Bitmap

/**
 * Specifies the file format of the screenshot.
 */
enum class ScreenshotFormat(val extension: String) {
  JPEG("jpg"),
  PNG("png"),
  WEBP("webp");

  val compression: Bitmap.CompressFormat
    get() = when (this) {
      JPEG -> Bitmap.CompressFormat.JPEG
      PNG -> Bitmap.CompressFormat.PNG
      WEBP -> Bitmap.CompressFormat.WEBP
    }
}
