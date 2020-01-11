package social.api.mock.user

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
    val basicPrefix =  "Basic "
    val users = mutableMapOf<String, User>()
    val creds = mutableMapOf<String, UserCredentials>()

    override fun setCredentials(userName: String, credentials: UserCredentials) {
        creds[userName] = credentials;
    }

    override fun validate(auth: UserAuth): ValidationResult {
        val authToken:String = auth.authToken ?: throw BadRequestException("Auth token was not supplied");
        if(authToken.startsWith(basicPrefix)) {
            val authToken = authToken.substring(basicPrefix.length)
            val (userName, password) = String(Base64.getDecoder().decode(authToken.toByteArray(StandardCharsets.UTF_8))).split(':')
            val knownUserCreds = creds[userName]
            if(knownUserCreds?.password.equals(password)) {
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