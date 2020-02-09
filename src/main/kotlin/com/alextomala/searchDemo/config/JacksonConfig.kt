package com.alextomala.searchDemo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonTokenId
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Configuration
class JacksonConfig(objectMapper: ObjectMapper) {
    init {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    }

    @Bean
    fun javaTimeModule() = JavaTimeModule()

    @Bean
    fun jdk8TimeModule() = Jdk8Module()
}

class OffsetDateTimeDeserializer : StdDeserializer<OffsetDateTime>(OffsetDateTime::class.java) {
    override fun deserialize(jp: JsonParser, ctxt: DeserializationContext?): OffsetDateTime {
        return if (jp.currentTokenId == JsonTokenId.ID_STRING) {
            try {
                OffsetDateTime.parse(jp.text, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss XXX"))
            } catch (e: DateTimeParseException) {
                throw e
            }
        } else {
            throw IllegalArgumentException("Date is bad")
        }
    }
}