package com.alextomala.searchDemo.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import java.util.*
import javax.persistence.*

@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy::class)
@Entity
data class Ticket(
        @Id @JsonProperty("_id") val id: UUID,
        val url: String,
        val externalId: String?,
//        val createdAt: OffsetDateTime,
        val type: String?,
        val subject: String,
        val description: String?,
        val priority: String,
        val status: String,
        @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val submitterId: Int?,
        @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val assigneeId: Int?,
        @Transient @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) val organizationId: Int?,
        @ElementCollection
        @CollectionTable(name = "ticket_tags", joinColumns = [JoinColumn(name = "ticket_id")])
        @Column(name = "tag")
        val tags: List<String>,
        val hasIncidents: Boolean,
//        val dueAt: OffsetDateTime,
        val via: String,

        @OneToOne
        @JoinColumn(name = "s_id")
        var submitter: User?,

        @OneToOne
        @JoinColumn(name = "a_id")
        var assignee: User?,

        @OneToOne
        @JoinColumn(name = "o_id")
        var organization: Organization?
)
