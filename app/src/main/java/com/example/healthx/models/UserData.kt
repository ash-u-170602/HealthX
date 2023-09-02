package com.example.healthx.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "userData")
data class UserData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val idAsDate: String,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "user_name")
    val userName: String?,
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "total_steps")
    val totalSteps: Int,
    @ColumnInfo(name = "steps")
    val steps: Int,
    @ColumnInfo(name = "calories")
    val calories: Int,
    @ColumnInfo(name = "distance")
    val distance: Int
)