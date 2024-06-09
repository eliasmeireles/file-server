package com.softwareplace.fileserver.security

import com.softwareplace.jsonlogger.log.kLogger
import com.softwareplace.springsecurity.authorization.AuthorizationHandler
import com.softwareplace.springsecurity.model.UserData
import jakarta.servlet.http.HttpServletRequest
import org.springframework.stereotype.Service

@Service
class AuthorizationHandlerImpl : AuthorizationHandler {
    override fun authorizationSuccessfully(request: HttpServletRequest, userData: UserData) {
        kLogger.info("Authorization successfully")
    }
}
