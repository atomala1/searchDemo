package com.alextomala.searchDemo.repository

import com.alextomala.searchDemo.model.User
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.full.memberProperties

interface UserRepository : CrudRepository<User, Int>, JpaSpecificationExecutor<User>


class UserSpecification(private val options: List<String>) : Specification<User>, BaseRepository<User>() {
    override fun toPredicate(root: Root<User>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val queries = fieldQueryToCriteria(root, builder, User::class.memberProperties, options)
        query.distinct(true)
        return builder.and(*queries.toTypedArray<Predicate>())
    }
}
