package social.api.mock

import javax.ws.rs.core.SecurityContext

object SecurityContextHolder {
    private val securityContextTL: ThreadLocal<SecurityContext> = ThreadLocal()
    fun set(sc: SecurityContext) {
        securityContextTL.set(sc)
    }

    fun get() = securityContextTL.get()

    fun getCurrentUserName() = get().userPrincipal.name
}