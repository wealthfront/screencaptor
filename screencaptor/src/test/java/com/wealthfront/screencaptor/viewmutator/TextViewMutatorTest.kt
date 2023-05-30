package com.wealthfront.screencaptor.viewmutator

import android.widget.TextView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class TextViewMutatorTest {
  @Test
  fun mutateView() {
    val attrSet = Robolectric.buildAttributeSet()
      .addAttribute(android.R.attr.text, "Old Text")
      .build()
    val textView = TextView(getApplicationContext(), attrSet)

    val mutator = TextViewMutator("New Text")
    mutator.mutate(textView)
    Truth.assertThat(textView.text).isEqualTo("New Text")
    mutator.restore(textView)
    Truth.assertThat(textView.text).isEqualTo("Old Text")
  }
}

