package social.api.mock.user

import social.api.mock.MockConstants
import social.api.mock.ResettableMock
import social.api.user.model.User
import social.api.user.model.UserAuth
import social.api.user.model.UserCredentials
import social.api.user.model.Users
import social.api.user.model.ValidationResult
import social.api.user.server.UserApiService
import java.nio.charset.StandardCharsets
import java.util.*
import javax.ws.rs.BadRequestException
import javax.ws.rs.NotFoundException

class UserApiServiceMock: UserApiService, ResettableMock {
    private val basicPrefix =  "Basic "
    private val bearerPrefix = "Bearer "
    private val users = mutableMapOf<String, User>()
    private val creds = mutableMapOf<String, UserCredentials>()

    override fun setCredentials(userName: String, credentials: UserCredentials) {
        creds[userName] = credentials;
    }

    override fun validate(auth: UserAuth): ValidationResult {
        val authToken:String = auth.authToken ?: throw BadRequestException("Auth token was not supplied");
        if(authToken.startsWith(basicPrefix)) {
            val authTokenRest = authToken.substring(basicPrefix.length)
            val (userName, password) = String(Base64.getDecoder().decode(authTokenRest.toByteArray(StandardCharsets.UTF_8))).split(':')
            val knownUserCreds = creds[userName]
            if(knownUserCreds?.password.equals(password)) {
                return ValidationResult().valid(true).userName(userName)
            }
        } else if(authToken.startsWith(bearerPrefix)) {
            val authTokenRest = authToken.substring(bearerPrefix.length)
            if(MockConstants.SYSTEM_AUTH_TOKEN.equals(authTokenRest)) {
                return ValidationResult().valid(true)
            }
        }
        return ValidationResult().valid(false)
    }

    override fun getUser(userName: String): User
            = users[userName] ?:  throw NotFoundException()

    override fun getUsers(): Users
            = Users().users(users.values.toList())

    override fun createUser(user: User): User {
        val userName = user.name ?: throw BadRequestException("User name is not set")
        users[userName] = user
        return user
    }

    override fun reset() {
        users.clear()
        creds.clear()
    }
}