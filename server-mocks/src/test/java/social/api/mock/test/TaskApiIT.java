package social.api.mock.test;

import org.junit.Test;
import social.api.task.model.Task;
import social.api.task.model.Tasks;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TaskApiIT extends AbstractApiTest {
    private static final Task task1 = new Task()
            .title("Find the cure")
            .creator(JOHN_NAME)
            .assignee(JANE_NAME);
    private static final Task task2 = new Task()
            .title("Test the cure")
            .creator(JANE_NAME)
            .assignee(JOHN_NAME);

    @Test
    public void testReturnTaskById() throws Exception {
        createAndAuthenticate(JOHN_NAME, JOHN_PASSWORD);
        String postedTaskId = postTasks(task1, task2).get(1);

        Task retrievedTask = taskApi.getTask(postedTaskId);

        assertEquals(postedTaskId, retrievedTask.getId());
        assertEquals(task2.getCreator(), retrievedTask.getCreator());
        assertEquals(task2.getAssignee(), retrievedTask.getAssignee());
        assertEquals(task2.getTitle(), retrievedTask.getTitle());
    }

    @Test
    public void testReturnAllTasks() throws Exception {
        createAndAuthenticate(JOHN_NAME, JOHN_PASSWORD);
        postTasks(task1, task2);

        Tasks retrievedTasksContainer = taskApi.getTasks(null);

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
