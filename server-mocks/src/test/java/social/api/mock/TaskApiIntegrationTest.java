package social.api.mock;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import social.api.infra.client.InfraApi;
import social.api.task.client.TaskApi;
import social.api.task.model.Task;
import social.api.task.model.Tasks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaskApiIntegrationTest {
    private static final String BASE_PATH = "http://127.0.0.1:8080/v1";

    private static final Task task1 = new Task()
            .title("Find the cure")
            .creator("John")
            .assignee("Mary");
    private static final Task task2 = new Task()
            .title("Test the cure")
            .creator("Mary")
            .assignee("John");

    TaskApi taskApi;
    InfraApi infraApi;
    MocksServer server;

    @Before
    public void init() throws Exception {
//        server = new MocksServer(BASE_PATH).start();

        taskApi = new TaskApi(new social.api.task.ApiClient().setBasePath(BASE_PATH));
        infraApi = new InfraApi(new social.api.infra.ApiClient().setBasePath(BASE_PATH));
        infraApi.restart();
    }

    @After
    public void tearDown() {
//        server.shutdown();
    }

    @Test
    public void testReturnTaskById() throws Exception {
        String postedTaskId = postTasks(task1, task2).get(1);

        Task retrievedTask = taskApi.getTask(postedTaskId);

        assertEquals(postedTaskId, retrievedTask.getId());
        assertEquals(task2.getCreator(), retrievedTask.getCreator());
        assertEquals(task2.getAssignee(), retrievedTask.getAssignee());
        assertEquals(task2.getTitle(), retrievedTask.getTitle());
    }


    @Test
    public void testReturnAllTasks() throws Exception {
        postTasks(task1, task2);

        Tasks retrievedTasksContainer = taskApi.getTasks();
        List<Task> retrievedTasks = retrievedTasksContainer.getTasks();

        assertEquals(2, retrievedTasks.size());
    }

    private List<String> postTasks(Task... tasks) throws Exception {
        List<String> taskIds = new ArrayList<String>();
        for(Task task: tasks) {
            taskIds.add(taskApi.createTask(task).getId());
        }
        return taskIds;
    }
}