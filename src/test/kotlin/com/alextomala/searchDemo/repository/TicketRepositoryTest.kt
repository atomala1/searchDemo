package com.alextomala.searchDemo.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class TicketRepositoryTest {
    @Autowired
    lateinit var ticketRepository: TicketRepository

    @Test
    fun `Assert All Are Loaded`() {
        Assertions.assertThat(ticketRepository.findAll().count()).isEqualTo(200)
    }

    @Test
    fun `Search - Null Date Field`() {
        val searchQuery = listOf("dueAt=")
        val found = ticketRepository.findAll(TicketSpecification(searchQuery))

        Assertions.assertThat(found.count()).isEqualTo(5)
        Assertions.assertThat(found[0].subject).isEqualTo("A Catastrophe in Korea (South)")
    }

    @Test
    fun `Search - List Field`() {
        val searchQuery = listOf("tags=Fédératéd Statés Of Micronésia")
        val found = ticketRepository.findAll(TicketSpecification(searchQuery))

        Assertions.assertThat(found.count()).isEqualTo(13)
        Assertions.assertThat(found[0].subject).isEqualTo("A Catastrophe in Cape Verde")
    }
}
