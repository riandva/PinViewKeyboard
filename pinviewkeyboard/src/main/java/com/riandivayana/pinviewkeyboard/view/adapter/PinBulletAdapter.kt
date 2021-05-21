package com.riandivayana.pinviewkeyboard.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.riandivayana.pinviewkeyboard.R
import com.riandivayana.pinviewkeyboard.databinding.RvPinBulletBinding

/**
 * Created by Rian Divayana on 4/30/21
 * Copyright (c) PT. TIMEDOOR INDONESIA
 */


class PinBulletAdapter()  : RecyclerView.Adapter<PinBulletAdapter.ViewHolder>() {
    //region properties
    var data = ""
    var pinCount = 0
    //endregion

    //region function
    fun setNewData(data: String) {
        this.data = data
        notifyDataSetChanged()
    }

    fun setCount(count: Int) {
        pinCount = count
        notifyDataSetChanged()
    }
    //endregion

    class ViewHolder(val binding: RvPinBulletBinding) : RecyclerView.ViewHolder(binding.root) {
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(RvPinBulletBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (position < data.count()) {
            holder.binding.imgBullet.setImageResource(R.drawable.ic_pin_inputted)
        } else {
            holder.binding.imgBullet.setImageResource(R.drawable.ic_pin_not_inputted)
        }
    }

    override fun getItemCount(): Int {
        return pinCount
    }
}