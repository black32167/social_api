package social.api.mock

import social.api.mock.control.AdminApiServiceMock
import social.api.mock.message.MessageApiServiceMock
import social.api.mock.task.TaskApiServiceMock
import social.api.mock.user.UserApiServiceMock
import java.io.IOException

class MocksServer(val baseUri: String) {
    private var jaxRsServer: JaxRsServer? = null

    @Throws(IOException::class)
    fun start(): MocksServer {
        check(jaxRsServer == null) { "Server is already run" }

        jaxRsServer = JaxRsServer(baseUri, createApiResources()).start()
        return this
    }

    fun shutdown() {
        jaxRsServer!!.shutdown()
        jaxRsServer = null
    }

    private fun createApiResources(): Array<Any> {
        val taskMock = TaskApiServiceMock()
        val userMock = UserApiServiceMock()
        val messageMock = MessageApiServiceMock()
        var adminMock = AdminApiServiceMock(listOf(taskMock, userMock, messageMock))
        return arrayOf(
                social.api.task.server.TaskApiResource(taskMock),
                social.api.admin.server.AdminApiResource(adminMock),
                social.api.user.server.UserApiResource(userMock),
                social.api.message.server.MessageApiResource(messageMock)
        )
    }
}
