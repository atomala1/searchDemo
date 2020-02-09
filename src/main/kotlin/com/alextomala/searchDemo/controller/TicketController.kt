package com.alextomala.searchDemo.controller

import com.alextomala.searchDemo.exception.EntityNotFoundException
import com.alextomala.searchDemo.model.Ticket
import com.alextomala.searchDemo.repository.TicketRepository
import com.alextomala.searchDemo.repository.TicketSpecification
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/tickets")
class TicketController(val ticketRepository: TicketRepository) {

    @GetMapping
    fun getAllTickets(): Iterable<Ticket> {
        return ticketRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getTicketById(@PathVariable("id") id: UUID): Ticket {
        return ticketRepository.findById(id)
                .orElseThrow { EntityNotFoundException("Ticket not found with id $id") }
    }

    @GetMapping("/search")
    fun searchTickets(@PathParam("query") query: String): List<Ticket> {
        val ticketSpecification = TicketSpecification(query.split(","))
        return ticketRepository.findAll(ticketSpecification).toList()
    }
}