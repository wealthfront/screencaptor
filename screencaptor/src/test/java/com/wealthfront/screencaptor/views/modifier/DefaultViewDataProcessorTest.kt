package com.wealthfront.screencaptor.views.modifier

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.common.truth.Truth.assertThat
import com.wealthfront.screencaptor.R
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment.application

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class DefaultViewDataProcessorTest {

  private lateinit var sampleView: View
  private lateinit var viewDataProcessor: ViewDataProcessor

  @Before
  fun setUp() {
    viewDataProcessor = DefaultViewDataProcessor()
    sampleView = LayoutInflater.from(application).inflate(R.layout.screenshot_test, null) as ViewGroup
  }

  @Test
  fun modifyViews() {
    assertThat(sampleView.findViewById<TextView>(R.id.textView).text).isEqualTo("Sample data")
    sampleView.findViewById<ImageView>(R.id.wealthfrontIcon).drawable.pixelsEqualTo(application.getDrawable(R.drawable.wf_logo))

    viewDataProcessor.modifyViews(sampleView, setOf(
      TextViewDataModifier(R.id.textView, "hidden"),
      ImageViewDataModifier(R.id.wealthfrontIcon, application.getDrawable(R.drawable.add_accounts)!!)
    ))

    assertThat(sampleView.findViewById<TextView>(R.id.textView).text).isEqualTo("hidden")
    sampleView.findViewById<ImageView>(R.id.wealthfrontIcon).drawable.pixelsEqualTo(application.getDrawable(R.drawable.add_accounts))
  }

  @Test
  fun resetViews() {
    val initialState = viewDataProcessor.modifyViews(sampleView, setOf(
      TextViewDataModifier(R.id.textView, "hidden"),
      ImageViewDataModifier(R.id.wealthfrontIcon, application.getDrawable(R.drawable.add_accounts)!!)
    ))

    assertThat(sampleView.findViewById<TextView>(R.id.textView).text).isEqualTo("hidden")
    sampleView.findViewById<ImageView>(R.id.wealthfrontIcon).drawable.pixelsEqualTo(application.getDrawable(R.drawable.add_accounts))

    viewDataProcessor.resetViews(sampleView, initialState)

    assertThat(sampleView.findViewById<TextView>(R.id.textView).text).isEqualTo("Sample data")
    sampleView.findViewById<ImageView>(R.id.wealthfrontIcon).drawable.pixelsEqualTo(application.getDrawable(R.drawable.wf_logo))
  }
}