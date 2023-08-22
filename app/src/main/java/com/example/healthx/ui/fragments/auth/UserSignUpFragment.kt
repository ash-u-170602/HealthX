package com.example.healthx.ui.fragments.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.healthx.auth.AuthViewModel
import com.example.healthx.auth.UserData
import com.example.healthx.databinding.UserSignupFragmentBinding
import com.example.healthx.ui.activities.OnboardingActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class UserSignUpFragment : Fragment() {

    private val binding by lazy { UserSignupFragmentBinding.inflate(layoutInflater) }
    private lateinit var viewModel: AuthViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private val RC_SIGN_IN = 40

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        if (auth.currentUser!=null){
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
                    moveToOnboardingScreen()
                    saveInFirebase()
                } else {
                    // Handle authentication failure
                }
            }
        }
    }


    private fun saveInFirebase() {
        val user = auth.currentUser

        if (user?.uid != null) {
            val users = UserData(
                userId = user.uid,
                userName = user.displayName,
                profilePictureUrl = user.photoUrl.toString(),
                email = user.email
            )

            val key = user.uid + " " + user.displayName
            database.reference.child("Users").child(key).setValue(users)
        }

    }

    private fun moveToOnboardingScreen() {
        val intent = Intent(requireContext(), OnboardingActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        requireActivity().finish()
    }


}