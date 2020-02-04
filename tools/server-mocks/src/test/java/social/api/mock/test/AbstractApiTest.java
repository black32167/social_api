package social.api.mock.test;

import org.junit.Before;
import social.api.admin.client.AdminApi;
import social.api.message.client.MessageApi;
import social.api.task.client.TaskApi;
import social.api.user.client.UserApi;
import social.api.user.model.User;
import social.api.user.model.UserCredentials;

import java.util.Arrays;
import java.util.List;

public abstract class AbstractApiTest {
    private static final String DEFAULT_BASE_PATH = "http://127.0.0.1:8081/v1";
    private static final String USER_BASE_PATH = System.getProperty("userApiPath", DEFAULT_BASE_PATH);
    private static final String TASK_BASE_PATH = System.getProperty("taskApiPath", DEFAULT_BASE_PATH);
    private static final String MESSAGE_BASE_PATH = System.getProperty("messageApiPath", DEFAULT_BASE_PATH);
    private static final String TASK_ADMIN_BASE_PATH = System.getProperty("taskAdminApiPath", DEFAULT_BASE_PATH);

    protected static final String JOHN_NAME = "John";
    protected static final String JOHN_PASSWORD = "JohnPassword";
    protected static final String JANE_NAME = "Jane";
    protected static final String JANE_PASSWORD = "JanePassword";

    protected final MessageApi messageApi = new MessageApi(new social.api.message.ApiClient().setBasePath(MESSAGE_BASE_PATH));
    protected final TaskApi taskApi = new TaskApi(new social.api.task.ApiClient().setBasePath(TASK_BASE_PATH));
    protected final UserApi userApi = new UserApi(new social.api.user.ApiClient().setBasePath(USER_BASE_PATH));
    private List<AdminApi> adminApis = Arrays.asList(new AdminApi(new social.api.admin.ApiClient().setBasePath(TASK_ADMIN_BASE_PATH)));

    @Before
    public void initAbstractApiIntegrationTest() throws Exception {
        for(AdminApi adminApi: adminApis) {
            adminApi.restart();
        }
    }

    protected void setupAuthentication(String userName, String userPassword) {
        messageApi.getApiClient().setUsername(userName);
        messageApi.getApiClient().setPassword(userPassword);
        taskApi.getApiClient().setUsername(userName);
        taskApi.getApiClient().setPassword(userPassword);
    }

    protected void cleanAuthentication() {
        setupAuthentication(null, null);
    }

    protected void createUser(String userName, String userPassword) throws Exception {
        User createdUser = userApi.createUser(new User().name(userName));
        userApi.setCredentials(createdUser.getName(), new UserCredentials().password(userPassword));
    }

    protected void createAndAuthenticate(String userName, String userPassword) throws Exception {
        createUser(userName, userPassword);
        setupAuthentication(userName, userPassword);
    }
}
