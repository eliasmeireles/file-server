package com.softwareplace.fileserver.security.model

import com.softwareplace.springsecurity.model.UserData

class InMemoryUser(
    private val username: String,
    private val password: String,
    private val authToken: String
) : UserData {
    override fun getUsername() = username

    override fun getPassword() = password

    override fun authToken() = authToken
}

