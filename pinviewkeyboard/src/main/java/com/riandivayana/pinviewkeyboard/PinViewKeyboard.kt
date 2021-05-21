package com.riandivayana.pinviewkeyboard

/**
 * Created by Rian Divayana on 19/05/21
 * Copyright (c) PT. TIMEDOOR INDONESIA
 */


import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import com.riandivayana.pinviewkeyboard.databinding.FragmentPinCodeBinding
import com.riandivayana.pinviewkeyboard.view.adapter.PinBulletAdapter
import com.riandivayana.pinviewkeyboard.view.adapter.PinNumberAdapter


/**
 * Created by Rian Divayana on 19/05/21
 * Copyright (c) PT. TIMEDOOR INDONESIA
 */

interface OnPinViewChangeListener {
    fun onPinChange(data: String)
    fun onPinReady(data: String)
    fun onPinNotReady()
}

class PinViewKeyboard : LinearLayout {

    //region properties
    private lateinit var binding: FragmentPinCodeBinding
    private var pinInterface: OnPinViewChangeListener? = null

    private val pinBulletAdapter = PinBulletAdapter()
    private val pinNumberAdapter = PinNumberAdapter() {
        onNumberPressed(it)
    }
    private var pinCount = 5

    //endregion

    constructor(context: Context) : super(context) {

    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {

        orientation = VERTICAL
        binding = FragmentPinCodeBinding.inflate(LayoutInflater.from(context), this)

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.PinView,
            0, 0).apply {

            try {
                pinCount = getInteger(R.styleable.PinView_pinCount, 5)

            } finally {
                recycle()
            }
        }
        pinBulletAdapter.setCount(pinCount)
        pinNumberAdapter.setCount(pinCount)

        binding.rvPinBullet.adapter = pinBulletAdapter
        (binding.rvPinBullet.layoutManager as GridLayoutManager).spanCount = pinCount
        binding.rvPinNumber.adapter = pinNumberAdapter

    }

    //region functions
    private fun onNumberPressed(text: String) {
        pinBulletAdapter.setNewData(text)
        pinInterface?.onPinChange(text)

        if (text.length >= pinCount) {
            pinInterface?.onPinReady(text)
        } else {
            pinInterface?.onPinNotReady()
        }
    }

    fun addOnPinViewChangeListener(listener: OnPinViewChangeListener) {
        pinInterface = listener
        pinInterface?.onPinNotReady()
    }

    fun getPin() : String {
        return pinBulletAdapter.data
    }

    fun resetPin() {
        pinBulletAdapter.setNewData("")
        pinNumberAdapter.setNewData("")
    }

    //endregion
}
