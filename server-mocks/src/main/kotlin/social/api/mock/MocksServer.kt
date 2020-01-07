package social.api.mock

import social.api.mock.control.MockApiServiceImpl
import social.api.mock.task.TaskApiServiceMock
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
        return arrayOf(
                social.api.task.server.TaskApiResource(taskMock),
                social.api.admin.server.AdminApiResource(MockApiServiceImpl(listOf(taskMock)))
        )
    }
}
