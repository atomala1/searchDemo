package com.alextomala.searchDemo.model

import com.alextomala.searchDemo.config.OffsetDateTimeDeserializer
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.time.OffsetDateTime
import javax.persistence.*


/**
 * I'm not normally a fan of putting both the database object and view object together.  I think it adds
 * many different annotations and makes any consumers very dependent on the database model.
 *
 * I just did it this way as a quick demo of reading/writing from a database.
 */
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@Entity
class Organization(
        @Id @JsonProperty("_id") val id: Int,
        val url: String,
        val externalId: String,
        val name: String,
        val details: String?,
        val sharedTickets: Boolean,

        @ElementCollection
        @CollectionTable(name = "organization_domain_names", joinColumns = [JoinColumn(name = "organization_id")])
        @Column(name = "domain_name")
        val domainNames: List<String>,

        @JsonDeserialize(using = OffsetDateTimeDeserializer::class)
        @Column(name = "created_at", columnDefinition = "TIMESTAMP WITH TIME ZONE")
        val createdAt: OffsetDateTime,

        @ElementCollection
        @CollectionTable(name = "organization_tags", joinColumns = [JoinColumn(name = "organization_id")])
        @Column(name = "tag")
        val tags: List<String>
)
