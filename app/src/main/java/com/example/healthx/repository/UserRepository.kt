package com.example.healthx.repository

import com.example.healthx.models.UserData
import com.example.healthx.db.UserDatabase

class UserRepository(
    private val db: UserDatabase
) {
    
    suspend fun upsert(user: UserData) = db.getUserDao().upsert(user)

    fun getUserData() = db.getUserDao().getUserData()

}