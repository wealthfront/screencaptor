package com.wealthfront.screencaptor.sample.impl

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SampleActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.screenshot_test)
    findViewById<Button>(R.id.showDialog).setOnClickListener {
      AlertDialog.Builder(this)
        .setMessage("I am a Dialog")
        .show()
    }

    val messageList = findViewById<RecyclerView>(R.id.messageList)
    val layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
    messageList.layoutManager = layoutManager

    val dividerItemDecoration = DividerItemDecoration(this, layoutManager.orientation)
    messageList.addItemDecoration(dividerItemDecoration)

    val messages = arrayOf("Bulldog", "Corgi", "Mastiff", "Labrador Retriever", "Border Collie")
    messageList.adapter = MessageAdapter(messages)
    messageList.adapter!!.notifyDataSetChanged()
  }
}