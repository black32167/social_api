package social.api.mock

import io.swagger.annotations.Authorization
import social.api.user.client.UserApi
import social.api.user.model.UserAuth
import social.api.user.model.ValidationResult
import java.security.Principal
import javax.ws.rs.NotAuthorizedException
import javax.ws.rs.container.ContainerRequestContext
import javax.ws.rs.container.ContainerRequestFilter
import javax.ws.rs.container.ResourceInfo
import javax.ws.rs.core.Context
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext
import javax.ws.rs.ext.Provider

@Provider
class AuthFilter(val userApi:UserApi) : ContainerRequestFilter {
    @Context
    private var resourceInfo: ResourceInfo? = null

    override fun filter(crc: ContainerRequestContext) {
        resourceInfo?.resourceMethod?.also {
            val hasAuthorization = it.isAnnotationPresent(Authorization::class.java)
            if(hasAuthorization) {
                authorize(crc)
            }
        }
    }

    fun authorize(crc: ContainerRequestContext) {
        val auth = crc.headers["Authorization"]?.get(0) ?: throw NotAuthorizedException("BasicAuth")
        val validationResult:ValidationResult = userApi.validate(UserAuth().authToken(auth)) ?: throw NotAuthorizedException("BasicAuth")
        if(validationResult.valid != true) {
            throw NotAuthorizedException(Response.status(401).entity("Not authorized").build())
        }
        val sc = object: SecurityContext {
            override fun isUserInRole(p0: String?): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getAuthenticationScheme(): String {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun getUserPrincipal(): Principal =
                    object: Principal {
                        override fun getName() = validationResult.userName
                    }

            override fun isSecure(): Boolean {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }
        }
        crc.securityContext = sc
        SecurityContextHolder.set(sc)
    }
}