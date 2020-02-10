package social.api.server.auth

import social.api.server.auth.factory.AuthFactory
import social.api.server.auth.factory.BasicAuthFactory
import social.api.server.auth.factory.BearerAuthFactory
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.ext.Provider

@Provider
class ApiAuthFilter(
        val authFactories:List<AuthFactory> = listOf(BasicAuthFactory(), BearerAuthFactory()))
    : ContainerRequestFilter {

    private val BASIC_PREFIX = "Basic"
    override fun filter(ctx: ContainerRequestContext) {
        ctx.headers.getFirst("Authorization")?.also { authToken ->
            authFactories
                    .map {it.desearialize(authToken)}
                    .first {it != null}
                    ?.also {ApiAuthContext.setAuth(it)}
        }
    }
}
