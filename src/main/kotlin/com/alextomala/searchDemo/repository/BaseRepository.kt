package com.alextomala.searchDemo.repository

import java.time.OffsetDateTime
import java.time.format.DateTimeParseException
import java.util.stream.Collectors
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.ListJoin
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.KProperty1

open class BaseRepository<T> {

    fun getStringProperties(root: Root<*>, builder: CriteriaBuilder, collection: Collection<KProperty1<*, *>>, key: String, value: String): List<Predicate> {
        return collection.stream()
                .filter { it.name == key && root.get<Any>(it.name).javaType == String::class.java }
                .map {
                    if (value.isNotBlank()) {
                        builder.like(builder.lower(root.get<String>(it.name)), "%${value.toLowerCase()}%")
                    } else {
                        builder.isNull(root.get<String>(it.name))
                    }
                }
                .collect(Collectors.toList())
    }

    fun getListProperties(root: Root<*>, builder: CriteriaBuilder, collection: Collection<KProperty1<*, *>>, key: String, value: String): List<Predicate> {
        return collection.stream()
                .filter { it.name == key && root.get<Any>(it.name).javaType == List::class.java }
                .map {
                    val join: ListJoin<T, String> = root.joinList(it.name)
                    builder.like(builder.lower(join), "%${value.toLowerCase()}%")
                }
                .collect(Collectors.toList())
    }

    fun getBooleanProperties(root: Root<*>, builder: CriteriaBuilder, collection: Collection<KProperty1<*, *>>, key: String, value: String): List<Predicate> {
        return collection.stream()
                .filter { it.name == key && root.get<Any>(it.name).javaType == Boolean::class.java }
                .map { builder.equal(root.get<String>(it.name), value.toBoolean()) }
                .collect(Collectors.toList())
    }

    fun getOffsetDateTimeProperties(root: Root<*>, builder: CriteriaBuilder, collection: Collection<KProperty1<*, *>>, key: String, value: String): List<Predicate> {
        try {
            return collection.stream()
                    .filter { it.name == key && root.get<Any>(it.name).javaType == OffsetDateTime::class.java }
                    .map {
                        if (value.isNotBlank()) {
                            builder.equal(root.get<String>(it.name), OffsetDateTime.parse(value))
                        } else {
                            builder.isNull(root.get<String>(it.name))
                        }
                    }
                    .collect(Collectors.toList())
        } catch (e: DateTimeParseException) {
            throw IllegalArgumentException("Value $value for parameter $key was not of type OffsetDateTime")
        }
    }
}