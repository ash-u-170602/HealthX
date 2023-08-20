package com.example.healthx.auth

data class UserData(
    val userId: String,
    val userName: String?,
    val profilePictureUrl: String?,
    val email: String?
)