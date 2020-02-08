package com.alextomala.searchDemo.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@Entity
data class User(
        @Id @JsonProperty("_id") val id: Int,
        val url: String,
        val externalId: String,
        val name: String,
        val alias: String?,
//        val createdAt: OffsetDateTime,
        val active: Boolean,
        val verified: Boolean,
        val shared: Boolean,
        val locale: String?,
        val timezone: String?,
//        val lastLoginAt: OffsetDateTime,
        val email: String?,
        val phone: String,
        val signature: String,
        @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val organizationId: Int?,
        @ElementCollection
        @CollectionTable(name = "user_tags", joinColumns = [JoinColumn(name = "user_id")])
        @Column(name = "tag")
        val tags: List<String>,
        val suspended: Boolean,
        val role: String,

        @OneToOne
        @JoinColumn(name = "o_id")
        var organization: Organization?
)
