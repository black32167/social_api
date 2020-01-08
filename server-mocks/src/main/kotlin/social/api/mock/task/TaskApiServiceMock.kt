package social.api.mock.task

import social.api.mock.ResettableMock
import social.api.task.model.Task
import social.api.task.model.Tasks
import social.api.task.server.TaskApiService
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.ws.rs.NotFoundException

class TaskApiServiceMock : TaskApiService, ResettableMock {
    private val tasks = ConcurrentHashMap<String, Task>()

    override fun reset() {
        tasks.clear()
    }

    override fun getTask(taskId: String): Task
            = tasks[taskId] ?: throw NotFoundException()

    override fun getTasks(): Tasks
            = Tasks().tasks(tasks.values.toList())

    override fun createTask(task: Task): Task {
        val taskId = UUID.randomUUID().toString()
        tasks[taskId] = task.apply { id = taskId }
        return task
    }
}