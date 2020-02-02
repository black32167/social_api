package social.api.mock

import social.api.message.client.MessageApi
import social.api.mock.control.AdminApiServiceMock
import social.api.mock.message.MessageApiServiceMock
import social.api.mock.task.TaskApiServiceMock
import social.api.mock.user.UserApiServiceMock
import social.api.server.JaxRsServer
import social.api.user.client.UserApi
import java.io.IOException

class MocksServer(val baseUri: String) {
    private var jaxRsServer: JaxRsServer? = null

    @Throws(IOException::class)
    fun start(): MocksServer {
        check(jaxRsServer == null) { "Server is already run" }

        jaxRsServer = JaxRsServer(baseUri)
                .instances(createApiResources())
                .instances(arrayOf(AuthFilter(UserApi(social.api.user.ApiClient().setBasePath(baseUri)))))
                .start()
        return this
    }

    fun shutdown() {
        jaxRsServer!!.shutdown()
        jaxRsServer = null
    }

    private fun createApiResources(): Array<Any> {
        val messageApiClient = social.api.message.ApiClient().setBasePath(baseUri)
        messageApiClient.setBearerToken(MockConstants.SYSTEM_AUTH_TOKEN)
        val messageApi = MessageApi(messageApiClient)
        val taskMock = TaskApiServiceMock(messageApi)
        val userMock = UserApiServiceMock()
        val messageMock = MessageApiServiceMock()
        val adminMock = AdminApiServiceMock(listOf(taskMock, userMock, messageMock))
        return arrayOf(
                social.api.task.server.TaskApiResource(taskMock),
                social.api.admin.server.AdminApiResource(adminMock),
                social.api.user.server.UserApiResource(userMock),
                social.api.message.server.MessageApiResource(messageMock)
        )
    }
}
