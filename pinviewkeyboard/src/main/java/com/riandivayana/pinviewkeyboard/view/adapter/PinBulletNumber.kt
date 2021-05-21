package com.riandivayana.pinviewkeyboard.view.adapter

import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.riandivayana.pinviewkeyboard.databinding.RvPinDeleteBinding
import com.riandivayana.pinviewkeyboard.databinding.RvPinNumberBinding

/**
 * Created by Rian Divayana on 4/30/21
 * Copyright (c) PT. TIMEDOOR INDONESIA
 */


class PinNumberAdapter(val listener: (String) -> Unit) : RecyclerView.Adapter<PinNumberAdapter.ViewHolder>() {

    //region properties
    private var data = ""
    private var pinCount = 0

    val TYPE_NUMBER = 0
    val TYPE_DELETE = 1
    //endregion

    //region function
    fun setNewData(data: String) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setCount(count:Int) {
        pinCount = count
        notifyDataSetChanged()
    }
    //endregion

    open class ViewHolder(view: View) : RecyclerView.ViewHolder(view)  {
    }

    class PinViewHolder(val binding: RvPinNumberBinding): ViewHolder(binding.root)  {
    }

    class DeleteViewHolder(val binding: RvPinDeleteBinding) : ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == TYPE_NUMBER) {
            PinViewHolder(RvPinNumberBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        } else {
            DeleteViewHolder(RvPinDeleteBinding.inflate(LayoutInflater.from(parent.context), parent, false))
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when (holder) {
            is PinViewHolder -> {
                when (position){
                    9 -> {
                        holder.binding.tvNumber.text = ""
                    }
                    10 -> {
                        holder.binding.tvNumber.text = "0"
                    }
                    else -> {
                        holder.binding.tvNumber.text = (position + 1).toString()
                    }
                }
                holder.binding.tvNumber.setOnClickListener {
                    if (data.length < pinCount) {
                        data+=holder.binding.tvNumber.text
                        listener(data)
                    }
                }
            }
            is DeleteViewHolder -> {
                holder.binding.imgDeletePin.setOnClickListener {
                    if (data.isNotEmpty()) {
                        this.data = data.dropLast(1)
                        listener(data)
                    }
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return 12
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 11) {
            TYPE_DELETE
        } else {
            TYPE_NUMBER
        }
    }
}