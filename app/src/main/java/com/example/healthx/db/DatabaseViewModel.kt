package com.example.healthx.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthx.HealthXApplication
import com.example.healthx.models.Stats
import com.example.healthx.models.UserData
import com.example.healthx.repository.UserRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch
import java.util.Calendar

class DatabaseViewModel : ViewModel() {

    private val userDatabase = UserDatabase(HealthXApplication.instance!!.baseContext)

    private val repository = UserRepository(userDatabase)

    val userDataLiveData = getUserData()

    fun saveUserDataToDatabase(currentUser: FirebaseUser?) = viewModelScope.launch {
        currentUser?.let {
            val user = UserData(
                id = 0,
                userId = it.uid,
                userName = it.displayName,
                profilePictureUrl = it.photoUrl.toString(),
                email = it.email,
                stats = returnList()
            )
            repository.upsert(user)
        }
    }

    private fun returnList(): List<Stats> {
        val list = mutableListOf<Stats>()
        list.add(Stats(0, 0, 0, 0, Calendar.getInstance().time))
        list.add(Stats(0, 0, 0, 0, Calendar.getInstance().time))

        return list
    }

    private fun getUserData() = repository.getUserData()

}