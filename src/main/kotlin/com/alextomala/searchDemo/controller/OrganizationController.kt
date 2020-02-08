package com.alextomala.searchDemo.controller

import com.alextomala.searchDemo.model.Organization
import com.alextomala.searchDemo.repository.OrganizationRepository
import com.alextomala.searchDemo.repository.OrganizationSpecification
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/organizations")
class OrganizationController(val organizationRepository: OrganizationRepository) {

    @GetMapping
    fun getAllOrganizations(): Iterable<Organization> {
        return organizationRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getOrganizationById(@PathVariable("id") id: Int): Organization {
        return organizationRepository.findById(id)
                .orElseThrow { RuntimeException("Organization not found") }
    }

    @GetMapping("/search")
    fun searchOrganizations(@PathParam("query") query: String): List<Organization> {
        val organizationSpecification = OrganizationSpecification(query.split(","))
        return organizationRepository.findAll(organizationSpecification).toList()
    }
}