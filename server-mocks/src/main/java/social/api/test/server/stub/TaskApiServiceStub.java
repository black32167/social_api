package social.api.test.server.stub;

import social.api.stub.task.model.Task;
import social.api.stub.task.server.TaskApiService;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

public class TaskApiServiceStub implements TaskApiService {
    public Response taskGet(SecurityContext securityContext) {
        return Response.ok(new Task().creator("user1")).build();
    }
}
