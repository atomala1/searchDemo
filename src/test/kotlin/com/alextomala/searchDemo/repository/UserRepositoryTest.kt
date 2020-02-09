package com.alextomala.searchDemo.repository

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
internal class UserRepositoryTest {
    @Autowired
    lateinit var userRepository: UserRepository

    @Test
    fun `Assert All Are Loaded`() {
        Assertions.assertThat(userRepository.findAll().count()).isEqualTo(75)
    }

    @Test
    fun `Search - Null Date Field`() {
        val searchQuery = listOf("name=Francisca Rasmussen")
        val found = userRepository.findAll(UserSpecification(searchQuery))

        Assertions.assertThat(found.count()).isEqualTo(1)
        Assertions.assertThat(found[0].name).isEqualTo("Francisca Rasmussen")
    }
}