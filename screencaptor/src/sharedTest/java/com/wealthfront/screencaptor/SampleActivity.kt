package com.wealthfront.screencaptor

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SampleActivity : AppCompatActivity() {

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