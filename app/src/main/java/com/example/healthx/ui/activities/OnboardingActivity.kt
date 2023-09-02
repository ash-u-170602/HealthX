package com.example.healthx.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.healthx.R
import com.example.healthx.databinding.ActivityOnboardingBinding
import com.example.healthx.util.Constants.ACTION_SHOW_PEDOMETER_FRAGMENT

class OnboardingActivity : AppCompatActivity() {

    private val binding by lazy { ActivityOnboardingBinding.inflate(layoutInflater) }

    companion object {
        var key: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        navigateToPedometerFragmentIfNeeded(intent)

        val bottomNav = binding.bottomNav
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNav.setupWithNavController(navController)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navigateToPedometerFragmentIfNeeded(intent!!)
    }


    private fun navigateToPedometerFragmentIfNeeded(intent: Intent) {
        if (intent.action == ACTION_SHOW_PEDOMETER_FRAGMENT) {
            val navHostFragment =
                supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
            navHostFragment.findNavController().navigate(R.id.action_global_pedometerFragment)
        }
    }

}