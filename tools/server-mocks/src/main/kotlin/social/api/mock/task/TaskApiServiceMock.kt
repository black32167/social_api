package social.api.mock.task

import social.api.message.client.MessageApi
import social.api.message.model.Message
import social.api.mock.ResettableMock
import social.api.mock.SecurityContextHolder
import social.api.task.model.Task
import social.api.task.model.Tasks
import social.api.task.server.TaskApiService
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.ws.rs.NotFoundException

class TaskApiServiceMock(var messageApi:MessageApi) : TaskApiService, ResettableMock {
    private val tasks = ConcurrentHashMap<String, Task>()

    override fun reset() {
        tasks.clear()
    }

    override fun getTask(taskId: String): Task
            = tasks[taskId] ?: throw NotFoundException()

    override fun getTasks(filtersSpec:String?): Tasks
            = Tasks().tasks(tasks.values.filter { filterTasks(it, filtersSpec) }.toList())

    override fun createTask(task: Task): Task {
        val taskId = UUID.randomUUID().toString()
        tasks[taskId] = task.apply { id = taskId }

        // Sending notification
        task.assignee?.also { assignee->
            val currentUserName = SecurityContextHolder.getCurrentUserName()
            messageApi.createMessage(
                    Message()
                            .sender(currentUserName)
                            .recipient(task.assignee)
                            .messageBody("You've been assigned a task '${task.title}'" ));
        }

        return task
    }

    private fun filterTasks(task: Task, filtersSpec: String?): Boolean {
        if(filtersSpec != null) {
            val filters = filtersSpec.split(',')
            for (filterSpec in filters) {
                val (name, value) = filterSpec.split(':')
                if (!task.javaClass.getDeclaredMethod("get" + name.capitalize()).invoke(task).equals(value)) {
                    return false
                }
            }
        }
        return true
    }
}
