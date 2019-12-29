package social.api.test.server;

import java.io.IOException;

/**
 * Entry point runs all mocking implementations.
 */
public class MocksRunner {
    private static final String DEFAULT_BASE_PATH = "http://127.0.0.1:8080";

    public static void main(String[] args) throws IOException {
        MocksServer server = new MocksServer(DEFAULT_BASE_PATH).start();
        System.in.read();
        server.shutdown();
    }
}
