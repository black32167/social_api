package social.api.test.server;

import org.junit.Test;
import social.api.stub.task.ApiClient;
import social.api.stub.task.ApiException;
import social.api.stub.task.client.TaskApi;
import social.api.stub.task.model.Task;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ApiIntegrationTest {
    private static final String BASE_PATH = "http://127.0.0.1:8080";

    @Test
    public void testIntegration() throws ApiException, IOException {
        ApiClient apiClient = new ApiClient().setBasePath(BASE_PATH);
        TaskApi api = new TaskApi(apiClient);
        Task task = api.taskGet();

        assertNotNull(task);
        assertEquals("user1", task.getCreator());
    }
}
