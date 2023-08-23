package com.example.healthx.db

import androidx.room.TypeConverter
import com.example.healthx.models.Stats
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class StatsTypeConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromStatsList(statsList: List<Stats>): String{
        return gson.toJson(statsList)
    }

    @TypeConverter
    fun toStatsList(statsListString: String): List<Stats>{
        val listType = object : TypeToken<List<Stats>>() {}.type
        return gson.fromJson(statsListString, listType)
    }

}