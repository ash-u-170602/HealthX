package com.example.healthx.auth

import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.healthx.HealthXApplication
import com.example.healthx.models.Stats
import com.example.healthx.models.UserData
import com.example.healthx.ui.activities.OnboardingActivity
import com.example.healthx.util.Constants.SERVER_CLIENT_ID
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase
import java.util.Calendar

class AuthViewModel : ViewModel() {

    private val _authState = MutableLiveData<AuthState>()
    val authState: LiveData<AuthState> get() = _authState

    private val _userKey = MutableLiveData<String?>()
    val userKey: LiveData<String?> get() = _userKey

    fun setUserKey(key: String?) {

        _userKey.value = key
        if (key != null) {
            OnboardingActivity.key = key
        }
    }


    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var mGoogleSignInClient: GoogleSignInClient
    private val database = FirebaseDatabase.getInstance()

    init {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(SERVER_CLIENT_ID)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(HealthXApplication.instance!!.baseContext, gso)
    }

    fun saveUserDataToFirebase(user: FirebaseUser?) {
        user?.let {
            val users = UserData(
                id = 0,
                userId = it.uid,
                userName = it.displayName,
                profilePictureUrl = it.photoUrl.toString(),
                email = it.email,
                stats = returnList()
            )

            val key = it.uid + " " + it.displayName
            setUserKey(key)
            database.reference.child("Users").child(key).setValue(users)
        }
    }

    fun signInWithGoogle() {
        mGoogleSignInClient.signOut().addOnCompleteListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            _authState.postValue(AuthState.GoogleSignIn(signInIntent))
        }
    }

    fun firebaseAuthWithGoogle(account: GoogleSignInAccount?): Task<AuthResult> {
        val idToken =
            account?.idToken ?: throw IllegalArgumentException("Account or ID token is null")

        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        return auth.signInWithCredential(credential)
    }

    fun returnList(): List<Stats> {
        val list = mutableListOf<Stats>()
        list.add(Stats("10000", "2000", "1200", "2400", Calendar.getInstance().time))
        list.add(Stats("10000", "5000", "1700", "7600", Calendar.getInstance().time))
        list.add(Stats("10000", "7000", "4200", "8400", Calendar.getInstance().time))

        return list
    }


    sealed class AuthState {
        object Authenticated : AuthState()
        data class GoogleSignIn(val intent: Intent) : AuthState()
    }
}
