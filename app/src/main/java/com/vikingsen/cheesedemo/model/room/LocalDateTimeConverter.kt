package com.vikingsen.cheesedemo.model.room

import android.arch.persistence.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

class LocalDateTimeConverter {
    @TypeConverter
    fun fromStringToLocalDateTime(value: String?): LocalDateTime? {
        val text = value ?: return null
        return when {
            text.isBlank() || text == "null" -> null
            else -> try {
                LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (ex: Exception) {
                throw IllegalArgumentException("Cannot parse date time text: " + value, ex)
            }
        }
    }

    @TypeConverter
    fun fromLocalDateTimeToString(value: LocalDateTime?): String? {
        return when(value) {
            null -> null
            else -> DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value)
        }
    }
}