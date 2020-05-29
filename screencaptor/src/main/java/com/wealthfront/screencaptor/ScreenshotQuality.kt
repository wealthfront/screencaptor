package com.wealthfront.screencaptor

/**
 * Specifies the compression quality of the screenshot.
 */
enum class ScreenshotQuality(val value: Int) {
  LOW(25),
  MID(50),
  HIGH(75),
  BEST(100)
}
