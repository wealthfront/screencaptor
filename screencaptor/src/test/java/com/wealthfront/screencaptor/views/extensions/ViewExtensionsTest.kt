package com.wealthfront.screencaptor.views.extensions

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.*
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
    assertThat(children.count()).isEqualTo(8)
    assertThat(children[0]).isInstanceOf(LinearLayout::class.java)
    assertThat(children[1]).isInstanceOf(NestedScrollView::class.java)
    assertThat(children[2]).isInstanceOf(LinearLayout::class.java)
    assertThat(children[3]).isInstanceOf(ImageView::class.java)
    assertThat(children[4]).isInstanceOf(EditText::class.java)
    assertThat(children[5]).isInstanceOf(TextView::class.java)
    assertThat(children[6]).isInstanceOf(ScrollView::class.java)
  }

  @Test
  fun getViewResourceName() {
    val resourceName = sampleView.findViewById<EditText>(R.id.textField).getViewResourceName()
    assertThat(resourceName).isEqualTo("com.wealthfront.screencaptor.test:id/textField")
  }
}