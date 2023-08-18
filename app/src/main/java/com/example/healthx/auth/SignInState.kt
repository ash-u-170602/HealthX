package com.example.healthx.auth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)