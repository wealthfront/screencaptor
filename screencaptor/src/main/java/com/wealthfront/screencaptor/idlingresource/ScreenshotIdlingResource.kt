package com.wealthfront.screencaptor.idlingresource

import androidx.test.espresso.IdlingResource

internal class ScreenshotIdlingResource: IdlingResource {
  private var resourceCallback: IdlingResource.ResourceCallback? = null
  private var screenshotCaptured = false

  override fun getName(): String = "ScreenCaptor#captureScreenshot"

  override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
    resourceCallback = callback
  }

  override fun isIdleNow(): Boolean = screenshotCaptured

  fun setScreenshotCaptured() {
    this.screenshotCaptured = true
    resourceCallback?.onTransitionToIdle()
  }
}