package com.example.healthx.util

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

fun todayDate(): String {
    val currentDate = Calendar.getInstance().time
    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.getDefault())
    return dateFormat.format(currentDate)
}