package com.example.healthx.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthx.R
import com.example.healthx.databinding.ActivityMainBinding
import com.example.healthx.databinding.UserRegistrationFragmentBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


}