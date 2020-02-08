package com.alextomala.searchDemo.config

import com.alextomala.searchDemo.model.Organization
import com.alextomala.searchDemo.model.Ticket
import com.alextomala.searchDemo.model.User
import com.alextomala.searchDemo.repository.OrganizationRepository
import com.alextomala.searchDemo.repository.TicketRepository
import com.alextomala.searchDemo.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.context.annotation.Bean
import org.springframework.stereotype.Component
import java.io.File

/**
 * The startup config file that reads in the json data files and dumps them into H2.
 *
 * In a production application, this file would be completely unnecessary because the data
 * would already be in the database.
 */
@Component
class StartupConfig(val objectMapper: ObjectMapper,
                    val organizationRepository: OrganizationRepository,
                    val userRepository: UserRepository,
                    val ticketRepository: TicketRepository) {

    @Bean
    fun getOrganizations() {
        val classloader = ClassLoader.getSystemClassLoader()

        val organizationString = File(classloader.getResource("organizations.json").file).readText(Charsets.UTF_8)

        val organizations = objectMapper.readValue<List<Organization>>(organizationString)
        println(organizations)

        organizations.forEach { organizationRepository.save(it) }
    }

    @Bean
    fun getUsers() {
        val classloader = ClassLoader.getSystemClassLoader()

        val usersString = File(classloader.getResource("users.json").file).readText(Charsets.UTF_8)

        val users = objectMapper.readValue<List<User>>(usersString)
        println(users)

        users
                .forEach {
                    it.organization = organizationRepository.findById(it.organizationId ?: -1).orElse(null)
                    userRepository.save(it)
                }
    }

    @Bean
    fun getTickets() {
        val classloader = ClassLoader.getSystemClassLoader()

        val ticketsString = File(classloader.getResource("tickets.json").file).readText(Charsets.UTF_8)

        val tickets = objectMapper.readValue<List<Ticket>>(ticketsString)

        tickets
                .forEach {
                    it.organization = organizationRepository.findById(it.organizationId ?: -1).orElse(null)
                    it.submitter = userRepository.findById(it.submitterId ?: -1).orElse(null)
                    it.assignee = userRepository.findById(it.assigneeId ?: -1).orElse(null)
                    ticketRepository.save(it)
                }

        println()
    }
}