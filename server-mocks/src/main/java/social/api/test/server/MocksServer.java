package social.api.test.server;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import social.api.test.server.stub.TaskApiServiceStub;

import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MocksServer {
    private final String baseUri;
    private HttpServer httpServer;

    public MocksServer(String baseUri) {
        this.baseUri = baseUri;
    }

    public MocksServer start() throws IOException {
        if(httpServer != null) {
            throw new IllegalStateException("Server is already run");
        }
        ResourceConfig config = new ResourceConfig();
        config.register(JacksonFeature.class);
        config.registerInstances(createApiResources());
        config.register(new LoggingFeature(Logger.getLogger(LoggingFeature.DEFAULT_LOGGER_NAME),
                Level.INFO, LoggingFeature.Verbosity.PAYLOAD_ANY, 10000));

        httpServer = GrizzlyHttpServerFactory.createHttpServer(URI.create(baseUri), config, false);
//        ServerConfiguration serverConfiguration = httpServer.getServerConfiguration();
//        final AccessLogBuilder builder = new AccessLogBuilder("access.log");
//        builder.instrument(serverConfiguration);
        httpServer.start();
        return this;
    }

    public void shutdown() {
        httpServer.shutdown();
        httpServer = null;
    }

    private Object[] createApiResources() {
        return new Object[] {
            new social.api.stub.task.server.TaskApi(new TaskApiServiceStub())
        };
    }
}
