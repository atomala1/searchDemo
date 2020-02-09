package com.alextomala.searchDemo.model

import com.alextomala.searchDemo.config.OffsetDateTimeDeserializer
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import javax.persistence.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@Entity
data class User(
        @Id @JsonProperty("_id") val id: Int,
        val url: String,
        val externalId: String,
        val name: String,
        val alias: String?,
        val active: Boolean,
        val verified: Boolean,
        val shared: Boolean,
        val locale: String?,
        val timezone: String?,
        val email: String?,
        val phone: String,
        val signature: String,
        val suspended: Boolean,
        val role: String,
        @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val organizationId: Int?,

        @JsonDeserialize(using = OffsetDateTimeDeserializer::class)
        @Column(name = "last_login_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val lastLoginAt: OffsetDateTime?,

        @JsonDeserialize(using = OffsetDateTimeDeserializer::class)
        @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val createdAt: OffsetDateTime,

        @ElementCollection
        @CollectionTable(name = "user_tags", joinColumns = [JoinColumn(name = "user_id")])
        @Column(name = "tag")
        val tags: List<String>,

        @OneToOne
        @JoinColumn(name = "o_id")
        var organization: Organization?
)
