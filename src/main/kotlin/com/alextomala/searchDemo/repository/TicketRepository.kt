package com.alextomala.searchDemo.repository

import com.alextomala.searchDemo.model.Ticket
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import java.util.*
import java.util.stream.Collectors
import javax.persistence.criteria.*
import kotlin.reflect.full.memberProperties

interface TicketRepository : CrudRepository<Ticket, UUID>, JpaSpecificationExecutor<Ticket>


class TicketSpecification(private val options: List<String>) : Specification<Ticket> {
    override fun toPredicate(root: Root<Ticket>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val extraQueries = fieldQueryToCriteria(root, builder, options)
        query.distinct(true)
        return builder.and(*extraQueries.toTypedArray<Predicate>())
    }

    private fun fieldQueryToCriteria(root: Root<*>, builder: CriteriaBuilder, fq: List<*>): List<Predicate> {
        val predicates = ArrayList<Predicate>()

        fq.asSequence().map { it.toString().split("=".toRegex()) }
                .filter { it.size in arrayOf(2) }
                .forEach { terms ->
                    predicates.addAll(
                            Ticket::class.memberProperties.stream()
                                    .filter { it.name == terms[0] && root.get<Any>(it.name).javaType == String::class.java }
                                    .map {
                                        if (terms[1].isNotBlank()) {
                                            builder.like(builder.lower(root.get<String>(it.name)), "%${terms[1].toLowerCase()}%")
                                        } else {
                                            builder.isNull(root.get<String>(it.name))
                                        }
                                    }
                                    .collect(Collectors.toList())
                    )

                    predicates.addAll(
                            Ticket::class.memberProperties.stream()
                                    .filter { it.name == terms[0] && root.get<Any>(it.name).javaType == List::class.java }
                                    .map {
                                        val join: ListJoin<Ticket, String> = root.joinList(it.name)
                                        builder.like(builder.lower(join), "%${terms[1].toLowerCase()}%")
                                    }
                                    .collect(Collectors.toList())
                    )

                    predicates.addAll(
                            Ticket::class.memberProperties.stream()
                                    .filter { it.name == terms[0] && root.get<Any>(it.name).javaType == Boolean::class.java }
                                    .map { builder.equal(root.get<String>(it.name), terms[1].toBoolean()) }
                                    .collect(Collectors.toList())
                    )
                }
        return predicates.toList()
    }
}
