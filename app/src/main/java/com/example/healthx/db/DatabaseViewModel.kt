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

    fun saveStatsDetailsToDatabase(steps: Int = 0, calories: Int = 0, distance: Int = 0): Stats{
        return Stats(1000, steps, calories, distance, Calendar.getInstance().time)
    }

    private fun returnList(): List<Stats> {
        val list = mutableListOf<Stats>()
        list.add(saveStatsDetailsToDatabase())

        return list
    }

    private fun getUserData() = repository.getUserData()

}