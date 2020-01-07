package social.api.mock;

import org.junit.Before;
import org.junit.Test;
import social.api.admin.client.AdminApi;
import social.api.task.client.TaskApi;
import social.api.task.model.Task;
import social.api.task.model.Tasks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaskApiIntegrationTest {
    private static final String DEFAUT_BASE_PATH = "http://127.0.0.1:8081/v1";
    private static final String TASK_BASE_PATH = System.getProperty("taskApiPath", DEFAUT_BASE_PATH);
    private static final String TASK_ADMIN_BASE_PATH = System.getProperty("taskAdminApiPath", DEFAUT_BASE_PATH);

    private static final Task task1 = new Task()
            .title("Find the cure")
            .creator("John")
            .assignee("Mary");
    private static final Task task2 = new Task()
            .title("Test the cure")
            .creator("Mary")
            .assignee("John");

    static final TaskApi taskApi = new TaskApi(new social.api.task.ApiClient().setBasePath(TASK_BASE_PATH));
    static List<AdminApi> adminApis = Arrays.asList(new AdminApi(new social.api.admin.ApiClient().setBasePath(TASK_ADMIN_BASE_PATH)));

    @Before
    public void init() throws Exception {
        for(AdminApi adminApi: adminApis) {
            adminApi.restart();
        }
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
