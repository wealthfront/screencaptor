package com.wealthfront.screencaptor

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog

class SampleActivity : Activity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.screenshot_test)
    findViewById<Button>(R.id.showDialog).setOnClickListener {
      AlertDialog.Builder(this)
        .setMessage("I am a Dialog")
        .show()
    }
  }
}