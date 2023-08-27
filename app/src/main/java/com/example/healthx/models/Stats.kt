package com.example.healthx.models

import java.util.Date

data class Stats(
    val totalSteps: Int,
    val steps: Int,
    val calories: Int,
    val distance: Int,
    val date: Date,
)
