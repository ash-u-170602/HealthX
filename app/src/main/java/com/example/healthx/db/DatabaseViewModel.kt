package com.example.healthx.db

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthx.models.UserData
import com.example.healthx.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class DatabaseViewModel(
    app: Application,
    private val repository: UserRepository
) : AndroidViewModel(app) {

    fun saveUserDataToDatabase(currentUser: FirebaseUser?) = viewModelScope.launch {
        currentUser?.let {
            val user = UserData(
                userId = it.uid,
                userName = it.displayName,
                profilePictureUrl = it.photoUrl.toString(),
                email = it.email,
                stats = null
            )
            repository.upsert(user)
        }
    }


}