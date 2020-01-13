# OpenAPI JAX-RS delegating resource generator

## Overview
This is a project to generate Resource and service implementation interface from 
an OpenAPI specification.

## Generated code example
The generator will create maven project producing small library containing repository class
and service interface which you can implement in your JAX-RS server.

### Resource class

```java
@Produces({"application/json"})
@Consumes({"application/json"})
@Path("")
public class TaskApiResource {
    private final TaskApiService delegate;
    @Context
    private SecurityContext securityContext;

    public TaskApiResource(TaskApiService delegate) {
        this.delegate = delegate;
    }

    @Path("/task")
    @POST
    @Authorization("basicAuth")
    public Task createTask(Task task) {
        return this.delegate.createTask(task);
    }

    @Path("/task/{taskId}")
    @GET
    @Authorization("basicAuth")
    public Task getTask(@PathParam("taskId") String taskId) {
        return this.delegate.getTask(taskId);
    }

    @Path("/task")
    @GET
    @Authorization("basicAuth")
    public Tasks getTasks(@QueryParam("filter") String filter) {
        return this.delegate.getTasks(filter);
    }
}
```

### Service interface

```java
public interface TaskApiService {
    Task createTask(Task var1);

    Task getTask(String var1);

    Tasks getTasks(String var1);
}
```