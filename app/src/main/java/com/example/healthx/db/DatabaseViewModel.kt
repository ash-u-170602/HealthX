package com.example.healthx.db

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.healthx.HealthXApplication
import com.example.healthx.models.UserData
import com.example.healthx.repository.UserRepository
import com.example.healthx.util.todayDate
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

class DatabaseViewModel : ViewModel() {

    private val userDatabase = UserDatabase(HealthXApplication.instance!!.baseContext)

    private val repository = UserRepository(userDatabase)

    val userDataLiveData = getUserData()

    /*fun saveCurrentStepsToDatabase(steps: Int, calories: Int, distance: Int) =
        viewModelScope.launch {

            val user = UserData(

            )

            repository.upsert(user)
        }*/

    fun saveDefaultUserDataToDatabase(currentUser: FirebaseUser?) = viewModelScope.launch {
        currentUser?.let {
            val user = UserData(
                idAsDate = todayDate(),
                userId = it.uid,
                userName = it.displayName,
                profilePictureUrl = it.photoUrl.toString(),
                email = it.email,
                totalSteps = 0,
                steps = 0,
                calories = 0f,
                distance = 0f
            )
            repository.upsert(user)
        }
    }


    fun updatePedometerDetails(userId: String, newSteps: Int, newCalories: Float, newDistance: Float) =
        viewModelScope.launch {
            repository.updatePedometerDetails(userId, newSteps, newCalories, newDistance)
        }


    private fun getUserData() = repository.getUserData()

}