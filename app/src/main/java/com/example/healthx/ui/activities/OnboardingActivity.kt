package com.example.healthx.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.healthx.R
import com.example.healthx.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOnboardingBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}