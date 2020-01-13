# Mock service

Simple in-memory implementation of APIs in Kotlin. Can be used to test integrations while developing
front-ends or other dependent services.

Contains Java black box tests running scenarios against APIs implementation using generated clients.

Typical test might look like:

```java
public class TaskApiIntegrationTest extends AbstractApiIntegrationTest {
    private static final Task task1 = ...
    private static final Task task2 = ...

    @Test
    public void testReturnTaskById() throws Exception {
        // Register test user and prepare test data using API calls
        createAndAuthenticate(JOHN_NAME, JOHN_PASSWORD);
        String postedTaskId = postTasks(task1, task2).get(1);

        // Run scenario
        Task retrievedTask = taskApi.getTask(postedTaskId);
        
        // Validate result
        assertEquals(postedTaskId, retrievedTask.getId());
        assertEquals(task2.getCreator(), retrievedTask.getCreator());
        assertEquals(task2.getAssignee(), retrievedTask.getAssignee());
        assertEquals(task2.getTitle(), retrievedTask.getTitle());
    }
//...
}
```
