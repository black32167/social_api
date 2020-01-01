package social.api.test.server;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import social.api.stub.task.ApiClient;
import social.api.stub.task.ApiException;
import social.api.stub.task.client.TaskApi;
import social.api.stub.task.model.Task;
import social.api.stub.task.model.Tasks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaskApiIntegrationTest {
    private static final String BASE_PATH = "http://127.0.0.1:8080";

    private static final Task task1 = new Task()
            .title("Find the cure")
            .creator("John")
            .assignee("Mary");
    private static final Task task2 = new Task()
            .title("Test the cure")
            .creator("Mary")
            .assignee("John");

    TaskApi api;
    MocksServer server;

    @Before
    public void init() throws IOException {
        server = new MocksServer(BASE_PATH).start();
        ApiClient apiClient = new ApiClient().setBasePath(BASE_PATH);
        api = new TaskApi(apiClient);
    }

    @After
    public void tearDown() {
        server.shutdown();
    }

    @Test
    public void testReturnTaskById() throws ApiException, IOException {
        String postedTaskId = postTasks(task1, task2).get(1);

        Task retrievedTask = api.taskTaskIdGet(postedTaskId);

        assertEquals(postedTaskId, retrievedTask.getId());
        assertEquals(task2.getCreator(), retrievedTask.getCreator());
        assertEquals(task2.getAssignee(), retrievedTask.getAssignee());
        assertEquals(task2.getTitle(), retrievedTask.getTitle());
    }


    @Test
    public void testReturnAllTasks() throws ApiException, IOException {
        postTasks(task1, task2);

        Tasks retrievedTasksContainer = api.taskGet();
        List<Task> retrievedTasks = retrievedTasksContainer.getTasks();

        assertEquals(2, retrievedTasks.size());
    }

    private List<String> postTasks(Task... tasks) throws ApiException {
        List<String> taskIds = new ArrayList<String>();
        for(Task task: tasks) {
            taskIds.add(api.taskPost(task).getId());
        }
        return taskIds;
    }
}
