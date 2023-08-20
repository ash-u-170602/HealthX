package com.example.healthx.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.healthx.R
import com.example.healthx.auth.SignInViewModel
import com.example.healthx.auth.UserData
import com.example.healthx.databinding.UserSignupFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase


class UserSignUpFragment : Fragment() {

    private val binding by lazy { UserSignupFragmentBinding.inflate(layoutInflater) }
    private lateinit var signInViewModel: SignInViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var mGoogleSignClient: GoogleSignInClient
    private lateinit var database: FirebaseDatabase
    val RC_SIGN_IN = 40

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        signInViewModel = ViewModelProvider(this)[SignInViewModel::class.java]
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val gso = GoogleSignInOptions.Builder()
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        mGoogleSignClient = GoogleSignIn.getClient(requireContext(), gso)


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

        binding.btnSignUp.setOnClickListener {

        }


        super.onViewCreated(view, savedInstanceState)
    }

    private fun signUpWithGoogle() {
        val intent = mGoogleSignClient.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {

            val task: Task<GoogleSignInAccount> =
                GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.result
                firebaseAuth(account.idToken)
            } catch (e: ApiException) {
                throw RuntimeException(e)
            }
        }
    }

    private fun firebaseAuth(idToken: String?) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user = auth.currentUser
                val users = user?.uid?.let {
                    user.email?.let { it1 ->
                        UserData(
                            userId = it,
                            userName = user.displayName,
                            profilePictureUrl = user.photoUrl.toString(),
                            email = it1
                        )
                    }
                }

                user?.uid?.let { database.reference.child("Users").child(it).setValue(users) }
            }
        }
    }

}