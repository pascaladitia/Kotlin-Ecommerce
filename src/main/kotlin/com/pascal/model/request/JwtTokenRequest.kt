package com.pascal.model.request

import com.pascal.contans.UserType
import com.pascal.utils.RoleBasedAuth
import com.pascal.utils.RoleHierarchy
import io.ktor.server.auth.*

data class JwtTokenRequest(val userId: String, val email: String, val userType: String) : Principal {

    fun hasAccessTo(role: UserType): Boolean {
        val currentUserType = try {
            UserType.valueOf(this.userType.uppercase())
        } catch (e: IllegalArgumentException) {
            return false
        }
        return RoleHierarchy.hasAccess(currentUserType, role)
    }

    fun hasRole(role: UserType): Boolean {
        return try {
            UserType.valueOf(this.userType.uppercase()) == role
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    fun getUserType(): UserType? {
        return try {
            UserType.valueOf(this.userType.uppercase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    fun isSuperAdmin(): Boolean = RoleBasedAuth.isSuperAdmin(this.userType)

    fun isAdmin(): Boolean = RoleBasedAuth.isAdmin(this.userType)

    fun isCustomer(): Boolean = RoleBasedAuth.isCustomer(this.userType)

    fun hasPermission(permission: com.pascal.utils.Permission): Boolean =
        com.pascal.utils.Permissions.hasPermission(this, permission)

    fun getPermissions(): Set<com.pascal.utils.Permission> {
        return com.pascal.utils.Permissions.getPermissions(this.getUserType() ?: return emptySet())
    }
}