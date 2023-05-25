package com.wealthfront.screencaptor.globalmutator

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application
import com.wealthfront.screencaptor.test.R

@RunWith(RobolectricTestRunner::class)
class ViewTreeMutatorTest {

  @Test
  fun testMutation() {
    val viewToBeMutated = LayoutInflater.from(application).inflate(R.layout.screenshot_test, null) as ViewGroup

    val scrollView = viewToBeMutated.findViewById<NestedScrollView>(R.id.scrollView)
    val anotherScrollView = viewToBeMutated.findViewById<ScrollView>(R.id.anotherScrollView)
    val textField = viewToBeMutated.findViewById<EditText>(R.id.textField)

    assertThat(scrollView.isVerticalScrollBarEnabled).isTrue()
    assertThat(anotherScrollView.isVerticalScrollBarEnabled).isTrue()
    assertThat(textField.isCursorVisible).isTrue()

    ViewTreeMutator.Builder()
      .addMutations(setOf(CursorHider, ScrollbarHider))
      .build()
      .mutate(listOf(viewToBeMutated))

    assertThat(scrollView.isVerticalScrollBarEnabled).isFalse()
    assertThat(scrollView.isHorizontalScrollBarEnabled).isFalse()
    assertThat(anotherScrollView.isVerticalScrollBarEnabled).isFalse()
    assertThat(anotherScrollView.isHorizontalScrollBarEnabled).isFalse()
    assertThat(textField.isCursorVisible).isFalse()
  }
}
