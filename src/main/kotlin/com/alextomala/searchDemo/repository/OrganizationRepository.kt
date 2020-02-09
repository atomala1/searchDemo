package com.alextomala.searchDemo.repository

import com.alextomala.searchDemo.model.Organization
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.repository.CrudRepository
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root
import kotlin.reflect.full.memberProperties

interface OrganizationRepository : CrudRepository<Organization, Int>, JpaSpecificationExecutor<Organization>

class OrganizationSpecification(private val options: List<String>) : Specification<Organization>, BaseRepository<Organization>() {
    override fun toPredicate(root: Root<Organization>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate? {
        val queries = fieldQueryToCriteria(root, builder, Organization::class.memberProperties, options)
        query.distinct(true)
        return builder.and(*queries.toTypedArray<Predicate>())
    }
}
