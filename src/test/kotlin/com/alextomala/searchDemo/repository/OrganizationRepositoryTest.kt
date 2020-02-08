package com.alextomala.searchDemo.repository

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class OrganizationRepositoryTest {

    @Autowired
    lateinit var organizationRepository: OrganizationRepository

    @Test
    fun `Assert All Are Loaded`() {
        assertThat(organizationRepository.findAll().count()).isEqualTo(26)
    }

    @Test
    fun `Test Search - Name is Populated`() {
        val searchQuery = listOf("name=Enthaze")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(1)
        assertThat(found[0].name).isEqualTo("Enthaze")
    }

    @Test
    fun `Test Search - Description is Empty`() {
        val searchQuery = listOf("details=")
        val found = organizationRepository.findAll(OrganizationSpecification(searchQuery))

        assertThat(found.count()).isEqualTo(1)
        assertThat(found[0].name).isEqualTo("RetaLive")
    }
}
