package com.alextomala.searchDemo.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException

@SpringBootTest
internal class OrganizationRepositoryTest {

    @Autowired
    lateinit var organizationRepository: OrganizationRepository

    @Test
    fun `Assert All Are Loaded`() {
        assertThat(organizationRepository.findAll().count()).isEqualTo(26)
    }

    @Test
    fun `Search - Name is Populated`() {
        val searchQuery = listOf("name=Enthaze")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(1)
        assertThat(found[0].name).isEqualTo("Enthaze")
    }

    @Test
    fun `Search - Details Field is Empty`() {
        val searchQuery = listOf("details=")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(1)
        assertThat(found[0].name).isEqualTo("RetaLive")
    }

    @Test
    fun `Search - Date Field`() {
        val searchQuery = listOf("createdAt=2016-05-21T16:10:28-05:00")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(1)
        assertThat(found[0].name).isEqualTo("Enthaze")
    }

    @Test
    fun `Search - Null Date Field`() {
        val searchQuery = listOf("createdAt=")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(0)
    }

    @Test
    fun `Search - Invalid Date Field`() {
        val searchQuery = listOf("createdAt=INVALID")

        Assertions.assertThrows(InvalidDataAccessApiUsageException::class.java) {
            organizationRepository.findAll(OrganizationSpecification(searchQuery))
        }
    }

    @Test
    fun `Search - Boolean Field - true`() {
        val searchQuery = listOf("sharedTickets=true")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(10)
        assertThat(found[0].name).isEqualTo("Sulfax")
    }

    @Test
    fun `Search - Boolean Field - false`() {
        val searchQuery = listOf("sharedTickets=asdf")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(16)
        assertThat(found[0].name).isEqualTo("Enthaze")
    }
}
