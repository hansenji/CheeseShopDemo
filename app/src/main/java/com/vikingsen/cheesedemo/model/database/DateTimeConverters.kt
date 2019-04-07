package com.vikingsen.cheesedemo.model.database

import androidx.room.TypeConverter
import org.threeten.bp.LocalDateTime
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter


class DateTimeConverters {
    @TypeConverter
    fun fromStringToLocalDateTime(value: String?): LocalDateTime? {
        val text = value ?: return null
        return when {
            text.isBlank() || text == "null" -> null
            else -> try {
                LocalDateTime.parse(value, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            } catch (ex: Exception) {
                throw IllegalArgumentException("Cannot parse date time text: $value", ex)
            }
        }
    }

    @TypeConverter
    fun fromLocalDateTimeToString(value: LocalDateTime?): String? = when(value) {
        null -> null
        else -> DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(value)
    }

    @TypeConverter
    fun fromStringToOffsetDateTime(value: String?): OffsetDateTime? {
        val text = value ?: return null
        return when {
            text.isBlank() || text == "null" -> null
            else -> try {
                OffsetDateTime.parse(value, DateTimeFormatter.ISO_DATE_TIME)
            } catch (ex: Exception) {
                throw IllegalArgumentException("Cannot parse date time text: $value", ex)
            }
        }
    }

    @TypeConverter
    fun fromOffsetDateTimeToString(value: OffsetDateTime?): String? = when(value) {
        null -> null
        else -> DateTimeFormatter.ISO_DATE_TIME.format(value)
    }
}

