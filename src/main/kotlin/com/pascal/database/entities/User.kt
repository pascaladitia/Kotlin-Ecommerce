package com.pascal.database.entities

import com.pascal.contans.UserType
import com.pascal.database.entities.base.BaseEntity
import com.pascal.database.entities.base.BaseEntityClass
import com.pascal.database.entities.base.BaseIdTable
import com.pascal.feature.auth.JwtConfig
import com.pascal.model.request.JwtTokenRequest
import com.pascal.utils.RoleHierarchy
import org.jetbrains.exposed.v1.core.dao.id.EntityID
import org.jetbrains.exposed.v1.javatime.datetime
import java.time.LocalDateTime

object UserTable : BaseIdTable("user") {
    val email = varchar("email", 255)
    val userType = enumerationByName<UserType>("user_type", 100)
    val password = varchar("password", 200)
    val otpCode = varchar("otp_code", 6)
    val otpExpiry = datetime("otp_expiry").nullable()
    val isVerified = bool("is_verified").default(false)
    val isActive = bool("is_active").default(true)
    override val primaryKey = PrimaryKey(id)

    // Create a composite unique index on email and userType
    init {
        uniqueIndex("email_userType_idx", email, userType)
    }
}

class UserDAO(id: EntityID<String>) : BaseEntity(id, UserTable) {
    companion object : BaseEntityClass<UserDAO>(UserTable, UserDAO::class.java)
    
    var email by UserTable.email
    var userType by UserTable.userType
    var password by UserTable.password
    var otpCode by UserTable.otpCode
    var otpExpiry by UserTable.otpExpiry
    var isVerified by UserTable.isVerified
    var isActive by UserTable.isActive

    fun response() = UserResponse(
        id.value,
        email,
        isVerified,
        userType,
        isActive,
        createdAt,
        updatedAt
    )

    fun loggedInWIthToken() = LoginResponse(
        response(), JwtConfig.tokenProvider(JwtTokenRequest(id.value, email, userType.name))
    )

    fun hasRole(role: UserType): Boolean = this.userType == role

    fun hasAccessTo(role: UserType): Boolean = RoleHierarchy.hasAccess(this.userType, role)

    fun isActiveAndVerified(): Boolean = isVerified && isActive

    fun isSuperAdmin(): Boolean = userType == UserType.SUPER_ADMIN

    fun isAdmin(): Boolean = userType == UserType.ADMIN || userType == UserType.SUPER_ADMIN

    fun isCustomer(): Boolean = userType == UserType.CUSTOMER || userType == UserType.ADMIN || userType == UserType.SUPER_ADMIN
}

data class UserResponse(
    val id: String,
    val email: String,
    val isVerified: Boolean?,
    var userType: UserType,
    val isActive: Boolean,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)

data class LoginResponse(val user: UserResponse?, val accessToken: String)
data class ChangePassword(val oldPassword: String, val newPassword: String)