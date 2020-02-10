package social.api.server

import org.glassfish.grizzly.http.server.HttpServer
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory
import org.glassfish.jersey.jackson.JacksonFeature
import org.glassfish.jersey.logging.LoggingFeature
import org.glassfish.jersey.server.ResourceConfig
import org.slf4j.bridge.SLF4JBridgeHandler
import social.api.server.auth.ApiAuthFilter
import java.io.IOException
import java.net.URI
import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger

class JaxRsServer(val baseUri: String) {
    companion object {
        init {
            SLF4JBridgeHandler.removeHandlersForRootLogger();
            SLF4JBridgeHandler.install();
            val l = Logger.getLogger("")
            l.setLevel(Level.parse(System.getProperty("api.server.log.level", "FINE")));
            val ch = ConsoleHandler()
            ch.level = Level.ALL
            l.addHandler(ch)
        }
    }
    private var httpServer: HttpServer? = null
    private val instances = mutableListOf<Any>()
    private val classes = mutableSetOf<Class<*>>()

    fun classes(_classes: Array<Class<Any>>): JaxRsServer {
        classes.addAll(_classes)
        return this
    }
    fun instances(_instances: Array<Any>): JaxRsServer {
        instances.addAll(_instances)
        return this
    }
    @Throws(IOException::class)
    fun start(): JaxRsServer {
        check(httpServer == null) { "Server is already run" }
        val config = ResourceConfig()
                .register(JacksonFeature::class.java)
        instances.forEach { i->config.register(i) }
        config
                .registerClasses(classes)
                .register(
                    LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                    Level.FINE, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000))
                .register(WebApplicationExceptionMapper())
                .register(ApiAuthFilter())

        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), config, false)
        httpServer!!.start()
        return this
    }

    fun shutdown() {
        httpServer!!.shutdown()
        httpServer = null
    }
}