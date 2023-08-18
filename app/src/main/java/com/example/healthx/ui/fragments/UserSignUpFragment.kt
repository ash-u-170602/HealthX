package com.example.healthx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.IntentSenderRequest
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.healthx.R
import com.example.healthx.auth.GoogleAuthUiClient
import com.example.healthx.auth.SignInViewModel
import com.example.healthx.auth.UserData
import com.example.healthx.databinding.UserSignupFragmentBinding
import com.example.healthx.ui.activities.MainActivity
import com.example.healthx.ui.activities.OnboardingActivity
import kotlinx.coroutines.launch
import kotlin.math.sin

class UserSignUpFragment : Fragment() {

    private val binding by lazy { UserSignupFragmentBinding.inflate(layoutInflater) }
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var googleAuthUiClient: GoogleAuthUiClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        val applicationContext = (activity as MainActivity).applicationContext
        googleAuthUiClient = GoogleAuthUiClient(
            applicationContext,
            com.google.android.gms.auth.api.identity.Identity.getSignInClient(applicationContext)
        )



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

        binding.google.setOnClickListener {

            signUpWithGoogle()

//            val intent = Intent(requireContext(), OnboardingActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//            startActivity(intent)
//            requireActivity().finish()

        }


        super.onViewCreated(view, savedInstanceState)
    }

    private fun signUpWithGoogle() {
        lifecycleScope.launch {
            val signInResult = googleAuthUiClient.signInWithIntent(
                intent = Intent(requireContext(), OnboardingActivity::class.java)
            )
            signInViewModel.onSignInResult(signInResult){
                val data= it
                //upload to firebase
                if (it !=null) {
                    signInViewModel.onSignInSuccess(it){
                        //call intent
                    }
                }
            }
            val signInIntentSender = googleAuthUiClient.signIn()
            IntentSenderRequest.Builder(
                signInIntentSender ?: return@launch
            ).build()
        }

    }

}