package com.example.healthx.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.healthx.auth.UserData

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(user: UserData)

    @Query("SELECT * FROM USERDATA")
    fun getUserData(): LiveData<UserData>

}