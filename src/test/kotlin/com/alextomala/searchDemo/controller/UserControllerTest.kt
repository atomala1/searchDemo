package com.alextomala.searchDemo.controller

import com.alextomala.searchDemo.model.User
import com.alextomala.searchDemo.repository.UserRepository
import com.alextomala.searchDemo.repository.UserSpecification
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
import java.util.*

@ExtendWith(SpringExtension::class)
@WebMvcTest(controllers = [UserController::class])
class UserControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockBean
    lateinit var repository: UserRepository

    private val testUser = User(
            id = 101,
            url = "http://initech.zendesk.com/api/v2/organizations/101.json",
            name = "Test Person",
            alias = "testy",
            active = true,
            verified = true,
            shared = true,
            locale = "",
            timezone = "",
            email = "",
            phone = "",
            signature = "",
            tags = listOf(),
            externalId = "9270ed79-35eb-4a38-a46f-35725197ea8d",
            organization = null,
            organizationId = null,
            suspended = false,
            role = "",
            createdAt = OffsetDateTime.now(),
            lastLoginAt = OffsetDateTime.now()
    )

    @Test
    fun `Parameter is missing`() {
        mockMvc.get("/users/search")
                .andExpect { MockMvcResultMatchers.status().isBadRequest }
                .andExpect { jsonPath("$.message", Matchers.`is`("Parameter specified as non-null is null: method com.alextomala.searchDemo.controller.UserController.searchUsers, parameter query")) }
    }

    @Test
    fun `Repository Returns Something`() {
        Mockito.`when`(repository.findAll(any(UserSpecification::class.java)))
                .thenReturn(listOf(testUser))

        mockMvc.get("/users/search?query=name=Test Person")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.jsonPath("$.name").value("Test Person") }
    }

    @Test
    fun `Repository Returns Nothing`() {
        Mockito.`when`(repository.findAll(any(UserSpecification::class.java)))
                .thenReturn(listOf())

        mockMvc.get("/users/search?query=name=Test Person")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.content().string("[]") }
    }

    @Test
    fun `GetById - User Not Found`() {
        Mockito.`when`(repository.findById(testUser.id))
                .thenReturn(Optional.empty())

        mockMvc.get("/users/${testUser.id}")
                .andExpect { MockMvcResultMatchers.status().isBadRequest }
                .andExpect { jsonPath("$.message", Matchers.`is`("User not found with id 101")) }
    }

    @Test
    fun `GetById - User Found`() {
        Mockito.`when`(repository.findById(testUser.id))
                .thenReturn(Optional.of(testUser))

        mockMvc.get("/users/${testUser.id}")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.jsonPath("$.name").value("Test Person") }
    }

    @Test
    fun `GetAll - Users Found`() {
        Mockito.`when`(repository.findAll())
                .thenReturn(listOf(testUser))

        mockMvc.get("/users")
                .andExpect { MockMvcResultMatchers.status().isOk }
                .andExpect { MockMvcResultMatchers.jsonPath("$.name").value("Test Person") }
    }
}