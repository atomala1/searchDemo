package com.alextomala.searchDemo.repository

import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.*
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.ListJoin
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.KProperty1

open class BaseRepository<T> {

    fun fieldQueryToCriteria(root: Root<*>, builder: CriteriaBuilder, collection: Collection<KProperty1<*, *>>, queryParameters: List<*>): List<Predicate> {
        val predicates = mutableListOf<Predicate>()

        queryParameters
                .map { it.toString().split("=".toRegex()) }
                .filter { it.size in arrayOf(2) }
                .map { terms ->
                    getOptionalPredicate(collection, root, builder, terms[0], terms[1])
                            .ifPresent { predicates.add(it) }
                }

        return predicates.toList()
    }

    private fun getOptionalPredicate(collection: Collection<KProperty1<*, *>>, root: Root<*>, builder: CriteriaBuilder, key: String, value: String): Optional<Predicate> {
        return collection.stream()
                .filter { it.name == key }
                .findFirst()
                .map { property ->
                    when (root.get<Any>(property.name).javaType) {
                        List::class.java -> handleListPredicate(property, root, builder, value)
                        Boolean::class.java -> handleBooleanPredicate(property, root, builder, value)
                        OffsetDateTime::class.java -> handleOffsetDateTimePredicate(property, root, builder, value)
                        Int::class.java -> handleIntegerProperty(property, root, builder, value)
                        UUID::class.java -> handleUUIDProperty(property, root, builder, value)
                        else -> handleStringPredicate(property, root, builder, value)
                    }
                }
    }

    private fun handleStringPredicate(property: KProperty1<*, *>, root: Root<*>, builder: CriteriaBuilder, value: String): Predicate {
        return if (value.isNotBlank()) {
            builder.like(builder.lower(root.get<String>(property.name)), "%${value.toLowerCase()}%")
        } else {
            builder.isNull(root.get<String>(property.name))
        }
    }

    private fun handleListPredicate(property: KProperty1<*, *>, root: Root<*>, builder: CriteriaBuilder, value: String): Predicate {
        val join: ListJoin<T, String> = root.joinList(property.name)
        return builder.like(builder.lower(join), "%${value.toLowerCase()}%")
    }

    private fun handleBooleanPredicate(property: KProperty1<*, *>, root: Root<*>, builder: CriteriaBuilder, value: String): Predicate {
        return builder.equal(root.get<String>(property.name), value.toBoolean())
    }

    private fun handleIntegerProperty(property: KProperty1<*, *>, root: Root<*>, builder: CriteriaBuilder, value: String): Predicate {
        try {
            return if (value.isNotBlank()) {
                builder.equal(root.get<String>(property.name), Integer.parseInt(value))
            } else {
                builder.isNull(root.get<String>(property.name))
            }
        } catch (e: NumberFormatException) {
            throw IllegalArgumentException("Value $value for parameter ${property.name} was not of type Integer")
        }
    }

    private fun handleUUIDProperty(property: KProperty1<*, *>, root: Root<*>, builder: CriteriaBuilder, value: String): Predicate {
        try {
            return if (value.isNotBlank()) {
                builder.equal(root.get<String>(property.name), UUID.fromString(value))
            } else {
                builder.isNull(root.get<String>(property.name))
            }
        } catch (e: IllegalArgumentException) {
            throw IllegalArgumentException("Value $value for parameter ${property.name} was not of type UUID")
        }
    }

    private fun handleOffsetDateTimePredicate(property: KProperty1<*, *>, root: Root<*>, builder: CriteriaBuilder, value: String): Predicate {
        try {
            return if (value.isNotBlank()) {
                builder.equal(root.get<String>(property.name), OffsetDateTime.parse(value))
            } else {
                builder.isNull(root.get<String>(property.name))
            }
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Value $value for parameter ${property.name} was not of type OffsetDateTime")
        }
    }
}