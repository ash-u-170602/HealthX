package com.example.healthx.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.healthx.R
import com.example.healthx.databinding.UserSignupFragmentBinding
import com.google.firebase.auth.EmailAuthProvider

class UserSignUpFragment : Fragment() {

    private val binding by lazy { UserSignupFragmentBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        binding.ttSignIn.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .setCustomAnimations(
                    R.anim.enter_from_right,
                    R.anim.exit_to_left,
                    R.anim.enter_from_left,
                    R.anim.exit_to_right
                )
                .replace(R.id.fragmentContainer, UserSignInFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.btnSignUp.setOnClickListener {
            val emailId = binding.emailId.text.toString()
            val password = binding.password.text.toString()
            val confPassword = binding.password.text.toString()


        }


        super.onViewCreated(view, savedInstanceState)
    }

}