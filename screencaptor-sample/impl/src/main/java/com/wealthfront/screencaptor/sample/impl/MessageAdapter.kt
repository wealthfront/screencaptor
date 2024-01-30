package com.wealthfront.screencaptor.sample.impl

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MessageAdapter(private val messages: Array<String>) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
    val view = LayoutInflater.from(parent.context)
      .inflate(R.layout.message_row_item, parent, false) as TextView

    return MessageViewHolder(view)
  }

  override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
    holder.view.text = messages[position]
  }

  override fun getItemCount(): Int = messages.size

  data class MessageViewHolder(
    val view: TextView
  ) : RecyclerView.ViewHolder(view)
}

