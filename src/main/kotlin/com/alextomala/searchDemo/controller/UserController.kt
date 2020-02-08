package com.alextomala.searchDemo.controller

import com.alextomala.searchDemo.model.User
import com.alextomala.searchDemo.repository.UserRepository
import com.alextomala.searchDemo.repository.UserSpecification
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.websocket.server.PathParam

@RestController
@RequestMapping("/users")
class UserController(val userRepository: UserRepository) {

    @GetMapping
    fun getAllUsers(): Iterable<User> {
        return userRepository.findAll()
    }

    @GetMapping("/{id}")
    fun getUserById(@PathVariable("id") id: Int): User {
        return userRepository.findById(id)
                .orElseThrow { RuntimeException("User not found") }
    }

    @GetMapping("/search")
    fun searchUsers(@PathParam("query") query: String): List<User> {
        val userSpecification = UserSpecification(query.split(","))
        return userRepository.findAll(userSpecification).toList()
    }
}
