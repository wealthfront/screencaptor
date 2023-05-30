package com.wealthfront.screencaptor.viewmutator

import android.view.View
import android.widget.TextView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class VisibilityViewMutatorTest {
  @Test
  fun mutateView() {
    val attrSet = Robolectric.buildAttributeSet()
      .addAttribute(android.R.attr.visibility, "visible")
      .build()
    val textView = TextView(getApplicationContext(), attrSet)

    val mutator = VisibilityViewMutator(View::class.java, View.INVISIBLE)
    mutator.mutate(textView)
    assertThat(textView.visibility).isEqualTo(View.INVISIBLE)
    mutator.restore(textView)
    assertThat(textView.visibility).isEqualTo(View.VISIBLE)
  }
}