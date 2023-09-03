package com.example.healthx.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.healthx.models.UserData

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserData)

    @Query("SELECT * FROM USERDATA")
    fun getUserData(): LiveData<List<UserData>>

    @Query("UPDATE userData SET steps = :newSteps, calories = :newCalories, distance = :newDistance WHERE id = :userId")
    suspend fun updatePedometerDetails(userId: String, newSteps: Int, newCalories: Float, newDistance: Float)

}