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
        val queries = fieldQueryToCriteria(root, builder, Ticket::class.memberProperties, options)
        query.distinct(true)
        return builder.and(*queries.toTypedArray<Predicate>())
    }
}
