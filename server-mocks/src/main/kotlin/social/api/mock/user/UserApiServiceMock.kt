package social.api.mock.user

import social.api.mock.ResettableMock
import social.api.user.model.User
import social.api.user.model.Users
import social.api.user.server.UserApiService
import java.util.*
import javax.ws.rs.NotFoundException

class UserApiServiceMock: UserApiService, ResettableMock {
    val users = mutableMapOf<String, User>()
    
    override fun getUser(userId: String): User
            = users[userId] ?:  throw NotFoundException()

    override fun getUsers(): Users
            = Users().users(users.values.toList())

    override fun createUser(user: User): User {
        val userId = UUID.randomUUID().toString()
        users[userId] = user.apply { id = userId }
        return user
    }

    override fun reset() {
        users.clear()
    }
}