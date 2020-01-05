package social.api.mock.control

import social.api.infra.model.RestartedResponse
import social.api.infra.server.InfraApiService
import social.api.mock.ResettableMock

class MockApiServiceImpl(val resettableMocks:Collection<ResettableMock>) : InfraApiService {
    override fun restart(): RestartedResponse {
        resettableMocks.forEach { it.reset() }
        return RestartedResponse();
    }
}