package com.wealthfront.screencaptor

import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction

interface ViewMutation {
  fun getViewInteraction(): ViewInteraction
  fun getPerformAction(): ViewAction
  fun getRestoreAction(): ViewAction
}