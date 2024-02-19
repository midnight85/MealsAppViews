package com.example.mealsappviews.db

import androidx.room.TypeConverter
import androidx.room.TypeConverters

@TypeConverters
class MealTypeConverter {
    @TypeConverter
    fun fromAnyToString(attribute: Any?):String{
        if(attribute==null) return ""
        return attribute.toString()
    }
    @TypeConverter
    fun fromStringToAny(attribute: String?):Any{
        if(attribute==null) return ""
        return attribute
    }
    @TypeConverter
    fun fromString(value: String?): List<String>? {
        return value?.split(",")
    }

    @TypeConverter
    fun listToString(list: List<String>?): String? {
        return list?.joinToString(separator = ",")
    }
}