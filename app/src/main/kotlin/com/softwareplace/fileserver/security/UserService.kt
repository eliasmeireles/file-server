package com.softwareplace.fileserver.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.softwareplace.fileserver.properties.AppProperties
import com.softwareplace.fileserver.rest.model.UserContentRest
import com.softwareplace.fileserver.rest.model.UserInfoRest
import com.softwareplace.fileserver.security.model.InMemoryUser
import com.softwareplace.springsecurity.encrypt.Encrypt
import com.softwareplace.springsecurity.exception.ApiBaseException
import com.softwareplace.springsecurity.model.RequestUser
import com.softwareplace.springsecurity.service.AuthorizationUserService
import com.softwareplace.springsecurity.util.ReadFilesUtils
import org.springframework.core.io.ResourceLoader
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service

@Service
class UserService(
    private val properties: AppProperties,
    private val objectMapper: ObjectMapper,
    private val resourceLoader: ResourceLoader
) : AuthorizationUserService {

    private fun inMemoryUsers(): List<InMemoryUser> {
        val resource = ReadFilesUtils.readFileBytes(resourceLoader, properties.authorizationPath)

        return objectMapper.readValue(resource, Array<InMemoryUser>::class.java).toList()
    }

    override fun findUser(user: RequestUser): InMemoryUser? {
        return inMemoryUsers().firstOrNull { it.username == user.username }
    }

    override fun findUser(authToken: String): InMemoryUser? {
        return inMemoryUsers().firstOrNull { it.authToken() == authToken }
    }

    override fun loadUserByUsername(username: String?): InMemoryUser? {
        return inMemoryUsers().firstOrNull { it.authToken() == username }
    }

    fun authorizationGen(userInfoRest: UserInfoRest): UserContentRest {
        val encrypt = Encrypt(userInfoRest.password)

        val user: InMemoryUser? = inMemoryUsers().firstOrNull { it.username == userInfoRest.username }

        if (user != null) {
            throw ApiBaseException(status = HttpStatus.BAD_REQUEST, message = "Username is not available.")
        }

        return UserContentRest(
            password = encrypt.encodedPassword,
            authToken = encrypt.authToken,
            username = userInfoRest.username
        )
    }
}
