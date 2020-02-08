package com.alextomala.searchDemo.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import javax.persistence.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@Entity
class Organization(
        @Id @JsonProperty("_id") val id: Int,
        val url: String,
        val externalId: String,
        val name: String,
        @ElementCollection
        @CollectionTable(name = "organization_domain_names", joinColumns = [JoinColumn(name = "organization_id")])
        @Column(name = "domain_name")
        val domainNames: List<String>,
//        val createdAt: OffsetDateTime,
        val details: String?,
        val sharedTickets: Boolean,
        @ElementCollection
        @CollectionTable(name = "organization_tags", joinColumns = [JoinColumn(name = "organization_id")])
        @Column(name = "tag")
        val tags: List<String>
)
