package social.api.mock.control

import social.api.mock.ResettableMock
import social.api.stub.mock.server.ResetApiService
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

class MockApiServiceImpl(val resettableMocks:Collection<ResettableMock>) : ResetApiService {
    override fun resetPost(p0: SecurityContext?): Response {
        resettableMocks.forEach { it.reset() }
        return Response.noContent().build()
    }
}