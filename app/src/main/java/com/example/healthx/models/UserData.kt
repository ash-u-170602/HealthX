package com.example.healthx.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userData")
data class UserData(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id:Int,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "user_name")
    val userName: String?,
    @ColumnInfo(name = "profile_picture_url")
    val profilePictureUrl: String?,
    @ColumnInfo(name = "email")
    val email: String?,
    @ColumnInfo(name = "stats_json")
    val stats: List<Stats>?
)