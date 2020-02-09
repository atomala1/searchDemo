package com.alextomala.searchDemo.repository

import com.alextomala.searchDemo.model.Ticket
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.full.memberProperties

interface TicketRepository : CrudRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket>


class TicketSpecification(private val options: List<String>) : Specification<Ticket>, BaseRepository<Ticket>() {
    override fun toPredicate(root: Root<Ticket>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val queries = fieldQueryToCriteria(root, builder, options)
        query.distinct(true)
        return builder.and(*queries.toTypedArray<Predicate>())
    }

    private fun fieldQueryToCriteria(root: Root<*>, builder: CriteriaBuilder, queryParameters: List<*>): List<Predicate> {
        return queryParameters.asSequence()
                .map { it.toString().split("=".toRegex()) }
                .filter { it.size in arrayOf(2) }
                .map { terms ->
                    getStringProperties(root, builder, Ticket::class.memberProperties, terms[0], terms[1]) +
                            getListProperties(root, builder, Ticket::class.memberProperties, terms[0], terms[1]) +
                            getBooleanProperties(root, builder, Ticket::class.memberProperties, terms[0], terms[1]) +
                            getOffsetDateTimeProperties(root, builder, Ticket::class.memberProperties, terms[0], terms[1])
                }
                .flatten()
                .toList()
    }
}
