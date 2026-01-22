package com.pascal.feature.auth

import at.favre.lib.crypto.bcrypt.BCrypt
import com.pascal.contans.Message
import com.pascal.contans.UserType
import com.pascal.database.entities.*
import com.pascal.model.request.ForgetPasswordRequest
import com.pascal.model.request.LoginRequest
import com.pascal.model.request.RegisterRequest
import com.pascal.model.request.ResetRequest
import com.pascal.model.response.Registration
import com.pascal.utils.*
import com.pascal.utils.extension.notFoundException
import com.pascal.utils.extension.query
import org.jetbrains.exposed.v1.core.and
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.core.neq
import java.time.LocalDateTime

class AuthService : AuthRepository {

    override suspend fun register(registerRequest: RegisterRequest): Any = query {
        validateRegisterRequest(registerRequest)

        val userTypeEnum = try {
            UserType.valueOf(registerRequest.userType.uppercase())
        } catch (e: IllegalArgumentException) {
            UserType.CUSTOMER
        }

        val existingUserSameType =
            UserDAO.find { UserTable.email eq registerRequest.email and (UserTable.userType eq userTypeEnum) }
                .singleOrNull()

        val existingUserDifferentType =
            UserDAO.find { UserTable.email eq registerRequest.email and (UserTable.userType neq userTypeEnum) }
                .singleOrNull()

        val otp = generateOTP()
        val now = LocalDateTime.now().plusHours(24) // 24 hours otp expire time

        if (existingUserSameType != null) {
            if (existingUserSameType.isVerified) {
                throw CommonException(Message.USER_ALREADY_EXIST_WITH_THIS_EMAIL)
            } else {
                // Resend OTP
                if (existingUserSameType.otpExpiry!! < LocalDateTime.now()) {
                    existingUserSameType.otpCode = otp
                    sendEmail(existingUserSameType.email, otp)
                    "${Message.NEW_OTP_SENT_TO} ${existingUserSameType.email}"
                } else {
                    throw CommonException(Message.OTP_ALREADY_SENT_WAIT_UNTIL_EXPIRY)
                }
            }
        } else {
            val inserted = UserDAO.new {
                email = registerRequest.email
                otpCode = otp
                otpExpiry = now
                password = BCrypt.withDefaults().hashToString(12, registerRequest.password.toCharArray())
                userType = userTypeEnum
            }

            // Create user profile
            UserProfileDAO.new {
                userId = inserted.id
            }

            // Send OTP
            sendEmail(inserted.email, otp)

            val messageSuffix = if (existingUserDifferentType != null) {
                ". You already have an account as ${existingUserDifferentType.userType}."
            } else {
                ""
            }

            Registration(
                inserted.id.value,
                registerRequest.email,
                message = "${Message.OTP_SENT_TO} ${inserted.email}$messageSuffix"
            )
        }
    }

    private fun validateRegisterRequest(request: RegisterRequest) {
        if (!ValidationUtils.validateEmail(request.email))
            throw ValidationException("Invalid email format")
        if (!ValidationUtils.validatePassword(request.password))
            throw ValidationException("Password must be at least 8 characters with at least one letter and one number")
        if (UserType.fromString(request.userType) == null)
            throw ValidationException("Invalid user type. Must be one of: CUSTOMER, SELLER, ADMIN, SUPER_ADMIN")
    }

    override suspend fun login(loginRequest: LoginRequest): LoginResponse = query {
        validateLoginRequest(loginRequest)

        val userTypeEnum = UserType.fromString(loginRequest.userType) ?: run {
            throw loginRequest.email.notFoundException()
        }

        val userEntity =
            UserDAO.find { UserTable.email eq loginRequest.email and (UserTable.userType eq userTypeEnum) }
                .toList().singleOrNull()

        userEntity?.let {
            if (BCrypt.verifyer().verify(
                    loginRequest.password.toCharArray(), it.password
                ).verified
            ) {
                if (it.isVerified) {
                    if (it.isActive) {
                        it.loggedInWIthToken()
                    } else {
                        throw CommonException(Message.ACCOUNT_DEACTIVATED)
                    }
                } else {
                    throw CommonException(Message.ACCOUNT_NOT_VERIFIED)
                }
            } else {
                throw PasswordNotMatch()
            }
        } ?: throw loginRequest.email.notFoundException()
    }

    private fun validateLoginRequest(request: LoginRequest) {
        if (!ValidationUtils.validateEmail(request.email))
            throw ValidationException("Invalid email format")
        if (UserType.fromString(request.userType) == null)
            throw ValidationException("Invalid user type. Must be one of: CUSTOMER, SELLER, ADMIN, SUPER_ADMIN")
        if (request.password.isBlank())
            throw ValidationException("Password cannot be empty")
    }

    override suspend fun otpVerification(userId: String, otp: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun changePassword(
        userId: String,
        changePassword: ChangePassword
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun forgetPassword(forgetPasswordRequest: ForgetPasswordRequest): String {
        TODO("Not yet implemented")
    }

    override suspend fun resetPassword(resetPasswordRequest: ResetRequest): Int {
        TODO("Not yet implemented")
    }

    override suspend fun changeUserType(
        currentUserId: String,
        targetUserId: String,
        newUserType: UserType
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun deactivateUser(currentUserId: String, targetUserId: String): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun activateUser(currentUserId: String, targetUserId: String): Boolean {
        TODO("Not yet implemented")
    }

}