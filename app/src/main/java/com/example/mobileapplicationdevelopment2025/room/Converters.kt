package com.example.mobileapplicationdevelopment2025.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromString(value: String?): Map<String, String>? {
        if (value == null) return null
        val mapType = object : TypeToken<Map<String, String>>() {}.type
        return gson.fromJson(value, mapType)
    }

    @TypeConverter
    fun fromMap(map: Map<String, String>?): String? {
        if (map == null) return null
        return gson.toJson(map)
    }

    @TypeConverter
    fun fromList(value: List<String>?): String {
        return gson.toJson(value ?: emptyList<String>())
    }

    @TypeConverter
    fun toList(value: String): List<String> {
        if (value.isEmpty()) return emptyList()
        val type = TypeToken.getParameterized(ArrayList::class.java, String::class.java).type
        return gson.fromJson(value, type) ?: emptyList()
    }
} 