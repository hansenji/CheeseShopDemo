package com.vikingsen.cheesedemo.model.webservice.converter


import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer

import org.threeten.bp.LocalDateTime
import org.threeten.bp.format.DateTimeFormatter

import java.io.IOException

class LocalDateTimeDeserializer : StdDeserializer<LocalDateTime>(LocalDateTime::class.java) {

    @Throws(IOException::class)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        return LocalDateTime.parse(p.valueAsString, DateTimeFormatter.ISO_LOCAL_DATE_TIME)
    }
}
