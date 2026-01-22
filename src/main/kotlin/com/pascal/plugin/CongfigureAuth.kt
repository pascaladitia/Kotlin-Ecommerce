package com.pascal.plugin

import com.pascal.contans.UserType
import com.pascal.feature.auth.JwtConfig
import com.pascal.model.request.JwtTokenRequest
import com.pascal.utils.RoleHierarchy
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuth() {
    install(Authentication) {
        jwt (RoleManagement.CUSTOMER.role) {
            provideJwtAuthConfig(this, RoleManagement.CUSTOMER)
        }
        jwt(RoleManagement.ADMIN.role) {
            provideJwtAuthConfig(this, RoleManagement.ADMIN)
        }
        jwt(RoleManagement.SUPER_ADMIN.role) {
            provideJwtAuthConfig(this, RoleManagement.SUPER_ADMIN)
        }
    }
}

fun provideJwtAuthConfig(jwtConfig: JWTAuthenticationProvider.Config, userRole: RoleManagement) {
    jwtConfig.verifier(JwtConfig.verifier)
    jwtConfig.validate { call ->
        val userId = call.payload.getClaim("userId").asString()
        val email = call.payload.getClaim("email").asString()
        val userTypeString = call.payload.getClaim("userType").asString()

        // Convert string to UserType enum
        val userType = try {
            UserType.valueOf(userTypeString.uppercase())
        } catch (e: IllegalArgumentException) {
            return@validate null
        }

        // Check if the user has access to the required role (role hierarchy)
        if (RoleHierarchy.hasAccess(userType, userRole.userType)) {
            JwtTokenRequest(userId, email, userTypeString)
        } else null
    }
}

enum class RoleManagement(val role: String) {
    SUPER_ADMIN("super_admin"),
    ADMIN("admin"),
    CUSTOMER("customer");

    val userType: UserType
        get() = when (this) {
            SUPER_ADMIN -> UserType.SUPER_ADMIN
            ADMIN -> UserType.ADMIN
            CUSTOMER -> UserType.CUSTOMER
        }

    companion object {
        fun fromUserType(userType: UserType): RoleManagement = when (userType) {
            UserType.SUPER_ADMIN -> SUPER_ADMIN
            UserType.ADMIN -> ADMIN
            UserType.CUSTOMER -> CUSTOMER
        }

        // Role hierarchy helpers
        val adminRoles = setOf(SUPER_ADMIN, ADMIN)
        val customerRoles = setOf(SUPER_ADMIN, ADMIN, CUSTOMER)
    }
}