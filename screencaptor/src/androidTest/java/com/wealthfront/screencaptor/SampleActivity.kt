package com.wealthfront.screencaptor

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class SampleActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.screenshot_test)
    findViewById<Button>(R.id.showToast).setOnClickListener {
      Toast.makeText(this, "I am a Toast message", Toast.LENGTH_LONG).show()
    }
  }
}