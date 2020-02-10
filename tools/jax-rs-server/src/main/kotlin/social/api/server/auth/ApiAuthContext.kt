package social.api.server.auth

object ApiAuthContext {
    var threadLocal = ThreadLocal<ApiAuth>()

    fun setAuth(auth: ApiAuth) {
        threadLocal.set(auth)
    }

    fun getAuth() = threadLocal.get()
}