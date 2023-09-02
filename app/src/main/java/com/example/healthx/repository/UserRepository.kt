package com.example.healthx.repository

import com.example.healthx.models.UserData
import com.example.healthx.db.UserDatabase

class UserRepository(
    private val db: UserDatabase
) {

    suspend fun upsert(user: UserData) = db.getUserDao().upsert(user)

    suspend fun updatePedometerDetails(
        userId: String,
        newSteps: Int,
        newCalories: Int,
        newDistance: Int
    ) = db.getUserDao().updatePedometerDetails(
        userId, newSteps, newCalories, newDistance
    )

    fun getUserData() = db.getUserDao().getUserData()

}