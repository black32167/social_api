package social.api.server.auth

sealed class ApiAuth
class BearerAuth(val bearerToken: String): ApiAuth()
class BasicAuth(val user: String, val password: String): ApiAuth()