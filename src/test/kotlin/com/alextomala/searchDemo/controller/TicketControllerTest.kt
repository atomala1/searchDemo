package com.alextomala.searchDemo.controller

import com.alextomala.searchDemo.model.Ticket
import com.alextomala.searchDemo.repository.TicketRepository
import com.alextomala.searchDemo.repository.TicketSpecification
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
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [TicketController::class])
class TicketControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var repository: TicketRepository

    private val testTicket = Ticket(
            id = UUID.randomUUID(),
            url = "http://initech.zendesk.com/api/v2/organizations/101.json",
            tags = listOf(),
            externalId = "9270ed79-35eb-4a38-a46f-35725197ea8d",
            type = "incident",
            subject = "A Nuisance in Egypt",
            description = "",
            priority = "",
            status = "",
            hasIncidents = false,
            via = "",
            assignee = null,
            assigneeId = null,
            organization = null,
            organizationId = null,
            submitter = null,
            submitterId = null
    )

    @Test
    fun `Parameter is missing`() {
        mockMvc.get("/tickets/search")
                .andExpect { MockMvcResultMatchers.status().isBadRequest }
                .andExpect { jsonPath("$.message", Matchers.`is`("Parameter specified as non-null is null: method com.alextomala.searchDemo.controller.TicketController.searchTickets, parameter query")) }
    }

    @Test
    fun `Repository Returns Something`() {
        Mockito.`when`(repository.findAll(any(TicketSpecification::class.java)))
                .thenReturn(listOf(testTicket))

        mockMvc.get("/tickets/search?query=name=Enthaze")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.jsonPath("$.name").value("Enthaze") }
    }

    @Test
    fun `Repository Returns Nothing`() {
        Mockito.`when`(repository.findAll(any(TicketSpecification::class.java)))
                .thenReturn(listOf())

        mockMvc.get("/tickets/search?query=name=Enthaze")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().string("[]") }
    }
}