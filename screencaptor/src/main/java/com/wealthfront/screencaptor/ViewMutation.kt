package com.wealthfront.screencaptor

import androidx.test.espresso.ViewAction
import androidx.test.espresso.ViewInteraction

interface ViewMutation {
  fun getPerformInteraction(): ViewInteraction
  fun getPerformAction(): ViewAction

  fun getRestoreInteraction(): ViewInteraction
  fun getRestoreAction(): ViewAction
}