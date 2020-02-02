package social.api.mock.control

import social.api.admin.model.RestartedResponse
import social.api.admin.server.AdminApiService
import social.api.mock.ResettableMock

class AdminApiServiceMock(val resettableMocks:Collection<ResettableMock>) : AdminApiService {
    override fun restart(): RestartedResponse {
        resettableMocks.forEach { it.reset() }
        return RestartedResponse();
    }
}