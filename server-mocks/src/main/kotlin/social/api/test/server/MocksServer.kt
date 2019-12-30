package social.api.test.server

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.logging.LoggingFeature
import org.glassfish.jersey.server.ResourceConfig
import social.api.test.server.stub.TaskApiServiceStub
import java.io.IOException
import java.net.URI
import java.util.logging.Level
import java.util.logging.Logger

class MocksServer(val baseUri: String) {
    private var httpServer: HttpServer? = null

    @Throws(IOException::class)
    fun start(): MocksServer {
        check(httpServer == null) { "Server is already run" }
        val config = ResourceConfig()
        config.register(JacksonFeature::class.java)
        config.registerInstances(*createApiResources())
        config.register(LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000))

        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), config, false)
        httpServer!!.start()
        return this
    }

    fun shutdown() {
        httpServer!!.shutdown()
        httpServer = null
    }

    private fun createApiResources(): Array<Any> {
        return arrayOf(social.api.stub.task.server.TaskApi(TaskApiServiceStub()))
    }
}