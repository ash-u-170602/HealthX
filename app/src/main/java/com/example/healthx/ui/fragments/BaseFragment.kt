package com.example.healthx.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthx.R
import com.example.healthx.ui.activities.MainActivity
import com.example.healthx.ui.activities.OnboardingActivity
import java.util.Calendar

open class BaseFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun setGreet(): String {
        val currentTime = Calendar.getInstance().time

        val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 4..11 -> "Good Morning"
            in 12..16 -> "Good Afternoon"
            in 17..20 -> "Good Evening"
            in 21..23 -> "Good Night"
            in 0..3 -> "Good Night"
            else -> "Good Night"
        }

        return greeting
    }

    fun navigationVisibility(isVisible:Boolean){
        ( activity as OnboardingActivity).navigationVisibility(isVisible)
    }

}