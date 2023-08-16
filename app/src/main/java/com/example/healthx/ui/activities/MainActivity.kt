package com.example.healthx.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.healthx.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }


}