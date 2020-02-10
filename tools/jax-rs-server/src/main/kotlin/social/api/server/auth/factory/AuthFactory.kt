package social.api.server.auth.factory

import social.api.server.auth.ApiAuth

interface AuthFactory {
    fun desearialize(token:String): ApiAuth?
}