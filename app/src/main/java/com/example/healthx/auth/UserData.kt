package com.example.healthx.auth

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userData")
data class UserData(
    @PrimaryKey
    val userId: String,
    val userName: String?,
    val profilePictureUrl: String?,
    val email: String?
)