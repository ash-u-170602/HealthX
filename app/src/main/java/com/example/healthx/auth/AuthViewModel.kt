package com.example.healthx.auth

import android.app.Application
import android.content.Intent
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.healthx.util.Constants.Companion.SERVER_CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mGoogleSignInClient: GoogleSignInClient

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(application.applicationContext, gso)
    }


    fun signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            _authState.postValue(AuthState.GoogleSignIn(signInIntent))
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?): Task<AuthResult> {
        val idToken = account?.idToken ?: throw IllegalArgumentException("Account or ID token is null")

        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential)
    }


    sealed class AuthState {
        object Authenticated : AuthState()
        data class GoogleSignIn(val intent: Intent) : AuthState()
    }
}