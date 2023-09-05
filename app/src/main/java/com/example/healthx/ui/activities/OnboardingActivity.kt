package com.example.healthx.ui.activities

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
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

    fun navigationVisibility(isVisible: Boolean) {
        binding.apply {
            bottomNav.clearAnimation()
            if (isVisible) {
                bottomNav.animate()
                    .translationY(0.0f)
                    .alpha(1.0f)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationStart(animation: Animator) {
                            super.onAnimationStart(animation)
                            bottomNav.visibility = View.VISIBLE
                            floatingActionButton.visibility = View.VISIBLE
                        }
                    })
            } else {
                bottomNav.animate()
                    .translationY(bottomNav.height.toFloat())
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator) {
                            super.onAnimationEnd(animation)
                            bottomNav.visibility = View.GONE
                            floatingActionButton.visibility = View.GONE
                        }
                    })
            }
        }
    }

}