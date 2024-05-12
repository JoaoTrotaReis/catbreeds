package com.joaoreis.catbreeds.catbreedlist.data.local

import androidx.room.TypeConverter

class ListConverter {
    @TypeConverter
    fun listToString(value: List<String>) = value.joinToString(separator = ",")

    @TypeConverter
    fun stringToList(value: String) = value.split(",").map { it.trim() }
}