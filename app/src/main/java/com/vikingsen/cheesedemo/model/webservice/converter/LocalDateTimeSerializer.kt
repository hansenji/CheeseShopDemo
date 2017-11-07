package com.vikingsen.cheesedemo.model.webservice.converter

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.ser.std.StdSerializer

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

import java.io.IOException


class LocalDateTimeSerializer : StdSerializer<LocalDateTime>(LocalDateTime::class.java) {

    @Throws(IOException::class)
    override fun serialize(value: LocalDateTime, gen: JsonGenerator, provider: SerializerProvider) {
        gen.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))
    }
}
