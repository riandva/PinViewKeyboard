package com.riandivayana

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.riandivayana.databinding.ActivityMainBinding
import com.riandivayana.pinviewkeyboard.OnPinViewChangeListener

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.pinView.addOnPinViewChangeListener(object : OnPinViewChangeListener {
            override fun onPinChange(data: String) {
                //your code here
            }

            override fun onPinReady(data: String) {
                //when pin fully inputted
            }

            override fun onPinNotReady() {
                //when pin not fully inputted
            }

        })
        //available function
        binding.pinView.getPin()
        binding.pinView.resetPin()
    }
}