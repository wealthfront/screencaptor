package com.wealthfront.screencaptor.viewmutator

import android.app.Application
import android.widget.ImageView
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import com.google.common.truth.Truth.assertThat
import com.wealthfront.screencaptor.test.R
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class ImageMutatorTest {
  @Test
  fun mutateView() {
    val context = getApplicationContext<Application>()
    val attrSet = Robolectric.buildAttributeSet()
      .addAttribute(android.R.attr.src, "@drawable/wf_logo")
      .build()
    val imageView = ImageView(context, attrSet)
    val newDrawable = context.resources.getDrawable(R.drawable.add_accounts)
    val mutator = ImageViewMutator(newDrawable)
    mutator.mutate(imageView)
    assertThat(imageView.drawable).isEqualTo(newDrawable)
    mutator.restore(imageView)
    assertThat(imageView.drawable).isNotEqualTo(newDrawable)
  }
}