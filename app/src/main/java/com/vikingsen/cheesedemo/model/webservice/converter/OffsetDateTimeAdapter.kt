package com.vikingsen.cheesedemo.model.webservice.converter

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter
import org.threeten.bp.OffsetDateTime
import org.threeten.bp.format.DateTimeFormatter

class OffsetDateTimeAdapter : TypeAdapter<OffsetDateTime>() {

    override fun write(writer: JsonWriter, value: OffsetDateTime) {
        writer.value(value.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME))
    }

    override fun read(reader: JsonReader): OffsetDateTime {
        return OffsetDateTime.parse(reader.nextString(), DateTimeFormatter.ISO_OFFSET_DATE_TIME)
    }
}