package social.api.mock.test;

import org.junit.Test;
import social.api.message.model.Message;
import social.api.task.model.Task;

import static org.junit.Assert.assertEquals;

/**
 * Tests integration between 'Task' and 'Message' services.
 */
public class TaskMessageIntegrationTest extends AbstractApiIntegrationTest {
    /**
     * Tests notification is sent to assignee when message is created.
     */
    @Test
    public void testTaskCreationSendsMessage() throws Exception {
        createUser(JANE_NAME, JANE_PASSWORD);
        createAndAuthenticate(JOHN_NAME, JOHN_PASSWORD);

        // Create task from behalf of John and assign it to Jane
        taskApi.createTask(new Task().assignee(JANE_NAME).title("Some task"));

        // Check incoming messages from behalf of Jane and get notification about the assigned task
        setupAuthentication(JANE_NAME, JANE_PASSWORD);
        Message message = messageApi.getIncomingMessages().getMessages().get(0);

        assertEquals(message.getSender(), JOHN_NAME);
    }
}
