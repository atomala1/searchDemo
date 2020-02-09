package com.alextomala.searchDemo.config

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.JsonTokenId
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException


@Configuration
class JacksonConfig {

    @Primary
    @Bean
    fun objectMapper(builder: Jackson2ObjectMapperBuilder): ObjectMapper {
        val objectMapper = builder.build<ObjectMapper>()
        objectMapper.registerModule(JavaTimeModule())
        objectMapper.registerModule(KotlinModule())
        return objectMapper
    }
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