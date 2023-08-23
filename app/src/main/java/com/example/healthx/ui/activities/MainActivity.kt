package com.example.healthx.ui.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.healthx.R
import com.example.healthx.auth.AuthViewModel
import com.example.healthx.databinding.ActivityMainBinding
import com.example.healthx.ui.fragments.auth.UserSignUpFragment

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, UserSignUpFragment())
            .commit()
    }



}