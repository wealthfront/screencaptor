package com.wealthfront.screencaptor.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.core.widget.NestedScrollView
import com.google.common.truth.Truth.assertThat
import com.wealthfront.screencaptor.R
import org.junit.Test

import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment

@Suppress("DEPRECATION")
@RunWith(RobolectricTestRunner::class)
class ViewExtensionsTest {

  private val sampleView = LayoutInflater.from(RuntimeEnvironment.application).inflate(R.layout.screenshot_test, null) as ViewGroup

  @Test
  fun getAllChildren() {
    val children = sampleView.getAllChildren()
    assertThat(children.count()).isEqualTo(9)
    assertThat(children[0]).isInstanceOf(NestedScrollView::class.java)
    assertThat(children[1]).isInstanceOf(LinearLayout::class.java)
    assertThat(children[2]).isInstanceOf(ImageView::class.java)
    assertThat(children[3]).isInstanceOf(EditText::class.java)
    assertThat(children[4]).isInstanceOf(TextView::class.java)
    assertThat(children[5]).isInstanceOf(Button::class.java)
    assertThat(children[6]).isInstanceOf(ScrollView::class.java)
  }

  @Test
  fun getViewResourceName() {
    val resourceName = sampleView.findViewById<EditText>(R.id.textField).getViewResourceName()
    assertThat(resourceName).isEqualTo("com.wealthfront.screencaptor.test:id/textField")
  }
}
