package com.dotjoo.aghslilnidelivery.ui.adapter

import com.dotjoo.aghslilnidelivery.databinding.ItemNotifactionBinding

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghslilnidelivery.data.response.NotificationItem

class NotifactionAdapter(
) : RecyclerView.Adapter<NotifactionAdapter.NotifactionViewHolder>() {
    var _binding: ItemNotifactionBinding? = null
    var notifactionsItemsList: ArrayList<NotificationItem> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }
    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): NotifactionViewHolder {
        _binding =
            ItemNotifactionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return NotifactionViewHolder(_binding!!)
    }

    fun removeItem(position: Int) {
        notifactionsItemsList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, notifactionsItemsList.size)
    }
    var time = ""
    var date = ""
    override fun onBindViewHolder(holder: NotifactionViewHolder, position: Int) {
        try {
            var currentItem = notifactionsItemsList[position]
            holder.binding.tvTitle.text = currentItem.title
            date = currentItem.created_at?.split("T")?.get(0).toString()
            holder.binding.tvDate.text = date
            holder.binding.tvDesc.text = currentItem.body
        } catch (e: Exception) {
        }
    }
    override fun getItemCount(): Int = notifactionsItemsList.size
    class NotifactionViewHolder(val binding: ItemNotifactionBinding) :
        RecyclerView.ViewHolder(binding.root)
}