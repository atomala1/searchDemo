package com.alextomala.searchDemo.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.InvalidDataAccessApiUsageException

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


    @Test
    fun `Search - UUID Field - Valid`() {
        val searchQuery = listOf("id=436bf9b0-1147-4c0a-8439-6f79833bff5b")
        val found = ticketRepository.findAll(TicketSpecification(searchQuery))

        Assertions.assertThat(found.count()).isEqualTo(1)
        Assertions.assertThat(found[0].url).isEqualTo("http://initech.zendesk.com/api/v2/tickets/436bf9b0-1147-4c0a-8439-6f79833bff5b.json")
    }

    @Test
    fun `Search - UUID Field - Invalid`() {
        val searchQuery = listOf("id=asdf")
        org.junit.jupiter.api.Assertions.assertThrows(InvalidDataAccessApiUsageException::class.java) {
            ticketRepository.findAll(TicketSpecification(searchQuery))
        }
    }
}
