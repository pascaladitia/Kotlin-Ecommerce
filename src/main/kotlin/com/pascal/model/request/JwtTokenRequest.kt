package com.pascal.model.request

import com.pascal.constants.UserType
import com.pascal.utils.RoleBasedAuth
import com.pascal.utils.RoleHierarchy
import io.ktor.server.auth.*

data class JwtTokenRequest(val userId: String, val email: String, val userType: String) : Principal {

    /**
     * Check if current user has access to a specific role (with hierarchy)
     */
    fun hasAccessTo(role: UserType): Boolean {
        val currentUserType = try {
            UserType.valueOf(this.userType.uppercase())
        } catch (e: IllegalArgumentException) {
            return false
        }
        return RoleHierarchy.hasAccess(currentUserType, role)
    }

    /**
     * Check if current user has specific role
     */
    fun hasRole(role: UserType): Boolean {
        return try {
            UserType.valueOf(this.userType.uppercase()) == role
        } catch (e: IllegalArgumentException) {
            false
        }
    }

    /**
     * Get current user type
     */
    fun getUserType(): UserType? {
        return try {
            UserType.valueOf(this.userType.uppercase())
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    /**
     * Check if user is super admin
     */
    fun isSuperAdmin(): Boolean = RoleBasedAuth.isSuperAdmin(this.userType)

    /**
     * Check if user is admin
     */
    fun isAdmin(): Boolean = RoleBasedAuth.isAdmin(this.userType)

    /**
     * Check if user is seller
     */
    fun isSeller(): Boolean = RoleBasedAuth.isSeller(this.userType)

    /**
     * Check if user is customer
     */
    fun isCustomer(): Boolean = RoleBasedAuth.isCustomer(this.userType)

    /**
     * Check if user has a specific permission
     */
    fun hasPermission(permission: com.pascal.utils.Permission): Boolean =
        com.pascal.utils.Permissions.hasPermission(this, permission)

    /**
     * Get user's permissions
     */
    fun getPermissions(): Set<com.pascal.utils.Permission> {
        return com.pascal.utils.Permissions.getPermissions(this.getUserType() ?: return emptySet())
    }
}