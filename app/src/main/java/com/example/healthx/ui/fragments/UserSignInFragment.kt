package com.example.healthx.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.healthx.R
import com.example.healthx.databinding.UserSigninFragmentBinding
import com.example.healthx.ui.activities.MainActivity

class UserSignInFragment: Fragment() {

    private val binding by lazy { UserSigninFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.ttSignUp.setOnClickListener {
            (activity as MainActivity).onBackPressed()
        }



        super.onViewCreated(view, savedInstanceState)
    }


}