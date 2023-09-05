package com.example.healthx.ui.fragments.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.healthx.auth.AuthViewModel
import com.example.healthx.databinding.UserSignupFragmentBinding
import com.example.healthx.db.DatabaseViewModel
import com.example.healthx.ui.activities.OnboardingActivity
import com.example.healthx.util.Constants.RC_SIGN_IN
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth


class UserSignUpFragment : Fragment() {

    private val binding by lazy { UserSignupFragmentBinding.inflate(layoutInflater) }
    private val viewModel: AuthViewModel by activityViewModels()
    private val databaseViewModel: DatabaseViewModel by activityViewModels()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        auth = FirebaseAuth.getInstance()
        auth.signOut()

        if (auth.currentUser != null) {
            moveToOnboardingScreen()
        }


        viewModel.authState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AuthViewModel.AuthState.Authenticated -> {

                    moveToOnboardingScreen()
                }

                is AuthViewModel.AuthState.GoogleSignIn -> {
                    startActivityForResult(state.intent, RC_SIGN_IN)
                }
            }
        }

        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

//        binding.ttSignIn.setOnClickListener {
//            parentFragmentManager.beginTransaction()
//                .setCustomAnimations(
//                    R.anim.enter_from_right,
//                    R.anim.exit_to_left,
//                    R.anim.enter_from_left,
//                    R.anim.exit_to_right
//                )
//                .replace(R.id.fragmentContainer, UserSignInFragment())
//                .addToBackStack(null)
//                .commit()
//        }

        binding.google.setOnClickListener {
            viewModel.signInWithGoogle()
        }




        super.onViewCreated(view, savedInstanceState)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN && resultCode == Activity.RESULT_OK) {
            val task: Task<AuthResult> = viewModel.firebaseAuthWithGoogle(
                GoogleSignIn.getSignedInAccountFromIntent(data).result
            )
            task.addOnCompleteListener { authTask ->
                if (authTask.isSuccessful) {
                    databaseViewModel.saveDefaultUserDataToDatabase(auth.currentUser)
                    moveToOnboardingScreen()
                } else {
                    // Handle authentication failure
                    Toast.makeText(requireContext(), "Something Went Wrong!!", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    private fun moveToOnboardingScreen() {
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }

    override fun onDestroy() {
        super.onDestroy()
//        auth.signOut()
    }

}