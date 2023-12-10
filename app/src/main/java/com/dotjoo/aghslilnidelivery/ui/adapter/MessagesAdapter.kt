package com.dotjoo.aghslilnidelivery.ui.adapter

import android.annotation.SuppressLint
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.data.response.Message
import com.dotjoo.aghslilnidelivery.databinding.ItemChatBinding

class MessagesAdapter : RecyclerView.Adapter<MessagesAdapter.MessageViewHolder>() {
    class MessageViewHolder(val binding: ItemChatBinding) :
        RecyclerView.ViewHolder(binding.root)

    var messages = mutableListOf<Message>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    @SuppressLint("NotifyDataSetChanged")
    fun addMessage(message: Message) {
        messages.add(message)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding = ItemChatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val context = holder.binding.root.context!!
        if (messages[position].direction == "send") {
            holder.binding.containerFrame.gravity = Gravity.START
            holder.binding.msgTv.setTextColor(ContextCompat.getColor(context, R.color.black))
        } else {
            holder.binding.containerFrame.gravity = Gravity.END
            holder.binding.msgTv.setTextColor(ContextCompat.getColor(context, R.color.blue))
        }
        holder.binding.msgTv.text = messages[position].message
    }

    override fun getItemCount(): Int {
        return messages.size
    }
}