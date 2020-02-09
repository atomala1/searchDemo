package com.alextomala.searchDemo.controller

import com.alextomala.searchDemo.model.Organization
import com.alextomala.searchDemo.repository.OrganizationRepository
import com.alextomala.searchDemo.repository.OrganizationSpecification
import org.hamcrest.Matchers
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.time.OffsetDateTime

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [OrganizationController::class])
class OrganizationControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var repository: OrganizationRepository

    private val testOrg = Organization(
            id = 101,
            name = "Enthaze",
            url = "http://initech.zendesk.com/api/v2/organizations/101.json",
            createdAt = OffsetDateTime.now(),
            domainNames = listOf(
                    "kage.com",
                    "ecratic.com",
                    "endipin.com",
                    "zentix.com"),
            details = "MegaCorp",
            sharedTickets = false,
            tags = listOf(),
            externalId = "9270ed79-35eb-4a38-a46f-35725197ea8d"
    )

    @Test
    fun `Parameter is missing`() {
        mockMvc.get("/organizations/search")
                .andExpect { MockMvcResultMatchers.status().isBadRequest }
                .andExpect { jsonPath("$.message", Matchers.`is`("Parameter specified as non-null is null: method com.alextomala.searchDemo.controller.OrganizationController.searchOrganizations, parameter query")) }
    }

    @Test
    fun `Repository Returns Something`() {
        Mockito.`when`(repository.findAll(any(OrganizationSpecification::class.java)))
                .thenReturn(listOf(testOrg))

        mockMvc.get("/organizations/search?query=name=Enthaze")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.jsonPath("$.name").value("Enthaze") }
    }

    @Test
    fun `Repository Returns Nothing`() {
        Mockito.`when`(repository.findAll(any(OrganizationSpecification::class.java)))
                .thenReturn(listOf())

        mockMvc.get("/organizations/search?query=name=Enthaze")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().string("[]") }
    }
}