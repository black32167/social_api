package social.api.mock.test.auth;

import org.junit.Test;
import social.api.mock.test.AbstractApiTest;
import social.api.task.model.Task;

import static org.junit.Assert.fail;

public class TaskApiAuthenticationIT extends AbstractApiTest {
    private final static Task TASK = new Task()
            .creator(JOHN_NAME)
            .assignee(JANE_NAME);

    @Test
    public void testGetTasksFailedWithoutAuthentication() throws Exception {
        try {
            taskApi.getTasks(null);
            fail("Should prevent getTasks for unauthenticated user");
        } catch (Exception e) {
        }
    }

    @Test
    public void testCreateTaskFailedWithoutAuthentication() throws Exception {
        try {
            taskApi.createTask(TASK);
            fail("Should prevent createTask for unauthenticated user");
        } catch (Exception e) {
        }
    }

    @Test
    public void testGetTaskFailedWithoutAuthentication() throws Exception {
        createAndAuthenticate(JOHN_NAME, JANE_PASSWORD);
        String postedTaskId = taskApi.createTask(TASK).getId();
        cleanAuthentication();

        try {
            taskApi.getTask(postedTaskId);
            fail("Should prevent getTask for unauthenticated user");
        } catch (Exception e) {
        }
    }
}
