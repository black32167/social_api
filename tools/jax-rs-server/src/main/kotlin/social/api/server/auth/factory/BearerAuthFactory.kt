package social.api.server.auth.factory

import social.api.server.auth.ApiAuth
import social.api.server.auth.BearerAuth

class BearerAuthFactory : AuthFactory {
    private val BEARER_PREFIX = "Bearer "
    override fun desearialize(token: String): ApiAuth? {
        if(!token.startsWith(BEARER_PREFIX)) {
            return null
        }

        val bearerToken = token.substring(BEARER_PREFIX.length)
        return BearerAuth(bearerToken)
    }
}