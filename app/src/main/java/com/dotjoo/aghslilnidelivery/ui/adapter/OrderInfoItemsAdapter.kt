package com.dotjoo.aghslilnidelivery.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dotjoo.aghslilnidelivery.R
import com.dotjoo.aghslilnidelivery.data.response.OrderInfoItem
import com.dotjoo.aghslilnidelivery.databinding.ItemOrderInfoBinding

class OrderInfoItemsAdapter(
 ) : RecyclerView.Adapter<OrderInfoItemsAdapter.ItemsCheckoutViewHolder>() {

    lateinit var context: Context
    var _binding: ItemOrderInfoBinding? = null
    var ordersList = mutableListOf<OrderInfoItem>()
        @SuppressLint("NotifyDataSetChanged") set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemsCheckoutViewHolder {
        context = parent.context
        _binding = ItemOrderInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ItemsCheckoutViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: ItemsCheckoutViewHolder, position: Int) {

        var currentItem = ordersList[position]
        holder.binding.tvTitle.setText(currentItem.item?.name)
        holder.binding.tvService.setText("("+currentItem.item?.service?.name+")")
        holder.binding.tvPrice.setText(currentItem.price.toString() +" "+ context.getText(R.string.sr))
        holder.binding.tvNumAddItional.setText(currentItem.count.toString() +"x")
    }

    private fun removeItem(position: Int) {


        ordersList.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, ordersList.size)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = ordersList.size

    class ItemsCheckoutViewHolder(val binding: ItemOrderInfoBinding) : RecyclerView.ViewHolder(binding.root)

}


