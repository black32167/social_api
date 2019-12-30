package social.api.test.server.stub

import social.api.stub.task.model.Task
import social.api.stub.task.model.Tasks
import social.api.stub.task.server.TaskApiService
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.ws.rs.core.Response
import javax.ws.rs.core.SecurityContext

class TaskApiServiceStub : TaskApiService {
    private val tasks = ConcurrentHashMap<String, Task>()

    override fun taskTaskIdGet(taskId: String, securityContext: SecurityContext): Response {
        val task:Task? = tasks[taskId]
        return if(task != null) {
            Response.ok(task).build()
        } else {
            Response.status(404).build()
        }
    }

    override fun taskGet(securityContext: SecurityContext): Response {
        val tasksContainer = Tasks().tasks(tasks.values.toList())
        return Response.ok(tasksContainer).build()
    }

    override fun taskPost(task: Task, securityContext: SecurityContext): Response? {
        val taskId = UUID.randomUUID().toString()
        tasks[taskId] = task.apply { id = taskId }
        return Response.ok(task).build()
    }
}