package com.wealthfront.screencaptor.recyclerviewmutator

import android.widget.TextView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class RecyclerViewTextMutatorTest {
  @Test
  fun mutateView() {
    val attrSet = Robolectric.buildAttributeSet()
      .addAttribute(android.R.attr.text, "Old Text")
      .build()
    val textView = TextView(getApplicationContext(), attrSet)
    RecyclerViewTextMutator("New Text").mutate(textView)
    assertThat(textView.text).isEqualTo("New Text")
  }
}