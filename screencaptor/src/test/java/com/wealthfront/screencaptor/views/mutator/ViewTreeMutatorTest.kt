package com.wealthfront.screencaptor.views.mutator

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import androidx.core.widget.NestedScrollView
import com.google.common.truth.Truth.assertThat
import com.wealthfront.screencaptor.R
import com.wealthfront.screencaptor.views.ViewTreeMutator
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application

@Suppress("DEPRECATION")
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
      .viewMutators(setOf(CursorHider, ScrollbarHider))
      .mutateView(viewToBeMutated)
      .mutate()

    assertThat(scrollView.isVerticalScrollBarEnabled).isFalse()
    assertThat(scrollView.isHorizontalScrollBarEnabled).isFalse()
    assertThat(anotherScrollView.isVerticalScrollBarEnabled).isFalse()
    assertThat(anotherScrollView.isHorizontalScrollBarEnabled).isFalse()
    assertThat(textField.isCursorVisible).isFalse()
  }
}
