package social.api.server.auth.factory

import social.api.server.auth.ApiAuth
import social.api.server.auth.BasicAuth
import java.nio.charset.Charset
import java.util.*

class BasicAuthFactory : AuthFactory {
    private val BASIC_PREFIX = "Basic"
    override fun desearialize(token: String): ApiAuth? {
        if(!token.startsWith(BASIC_PREFIX)) {
            return null
        }

        val decodedBasicTokenBytes = Base64.getDecoder().decode(token.substring(BASIC_PREFIX.length))
        val (username, password) = String(decodedBasicTokenBytes, Charset.forName("UTF-8")).split(':')
        return BasicAuth(username, password)
    }
}