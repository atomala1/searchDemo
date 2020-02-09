package com.alextomala.searchDemo.config

import com.alextomala.searchDemo.model.Organization
import com.alextomala.searchDemo.model.Ticket
import com.alextomala.searchDemo.model.User
import com.alextomala.searchDemo.repository.OrganizationRepository
import com.alextomala.searchDemo.repository.TicketRepository
import com.alextomala.searchDemo.repository.UserRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.core.io.Resource
import org.springframework.stereotype.Component

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
    fun getOrganizations(@Value("classpath:organizations.json") resourceFile: Resource): List<Organization> {
        val organizationString = resourceFile.file.readText(Charsets.UTF_8)
        val organizations = objectMapper.readValue<List<Organization>>(organizationString)

        organizations.forEach { organizationRepository.save(it) }

        return organizations
    }

    @Bean
    fun getUsers(@Value("classpath:users.json") resourceFile: Resource): List<User> {
        val usersString = resourceFile.file.readText(Charsets.UTF_8)
        val users = objectMapper.readValue<List<User>>(usersString)

        users
                .forEach {
                    it.organization = organizationRepository.findById(it.organizationId ?: -1).orElse(null)
                    userRepository.save(it)
                }

        return users
    }

    @Bean
    fun getTickets(@Value("classpath:tickets.json") resourceFile: Resource): List<Ticket> {
        val ticketsString = resourceFile.file.readText(Charsets.UTF_8)
        val tickets = objectMapper.readValue<List<Ticket>>(ticketsString)

        tickets
                .forEach {
                    it.organization = organizationRepository.findById(it.organizationId ?: -1).orElse(null)
                    it.submitter = userRepository.findById(it.submitterId ?: -1).orElse(null)
                    it.assignee = userRepository.findById(it.assigneeId ?: -1).orElse(null)
                    ticketRepository.save(it)
                }

        return tickets
    }
}