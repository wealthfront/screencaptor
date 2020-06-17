package com.wealthfront.screencaptor.views.modifier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.common.truth.Truth.assertThat
import com.wealthfront.screencaptor.R
import com.wealthfront.screencaptor.views.modifier.ViewVisibilityModifier
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class ViewVisibilityModifierTest {

  private lateinit var sampleView: View

  @Before
  fun setUp() {
    sampleView = LayoutInflater.from(RuntimeEnvironment.application).inflate(R.layout.screenshot_test, null) as ViewGroup
  }

  @Test
  fun hideViews() {
    assertThat(sampleView.findViewById<EditText>(R.id.textField).visibility).isEqualTo(View.VISIBLE)

    ViewVisibilityModifier.hideViews(sampleView, setOf(R.id.textField))

    assertThat(sampleView.findViewById<EditText>(R.id.textField).visibility).isEqualTo(View.INVISIBLE)
  }

  @Test
  fun showViews() {
    val initialState = ViewVisibilityModifier.hideViews(sampleView, setOf(R.id.textField))

    assertThat(sampleView.findViewById<EditText>(R.id.textField).visibility).isEqualTo(View.INVISIBLE)

    ViewVisibilityModifier.showViews(sampleView, setOf(R.id.textField), initialState)

    assertThat(sampleView.findViewById<EditText>(R.id.textField).visibility).isEqualTo(View.VISIBLE)
  }
}