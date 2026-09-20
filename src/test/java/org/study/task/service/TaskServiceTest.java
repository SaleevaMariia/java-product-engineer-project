package org.study.task.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.study.task.api.CreateTaskRequest;
import org.study.task.api.TaskResponse;
import org.study.task.api.TaskStatus;
import org.study.task.exception.TaskNotFoundException;
import org.study.task.repository.InMemoryTaskRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

class TaskServiceTest {

    private TaskService taskService;

    @BeforeEach
    void setUp(){
        taskService = new TaskService(new InMemoryTaskRepository());
    }


    @Test
    void createTaskShouldReturnTaskWithId() {
        CreateTaskRequest request = createTaskRequest("title1");
        TaskResponse response = taskService.createTask(request);
        assertNotNull(response.id());
    }

    @Test
    void createTaskShouldSetDefaultStatusTodo() {
        CreateTaskRequest request = createTaskRequest("title2");
        TaskResponse response = taskService.createTask(request);
        assertEquals(TaskStatus.TODO, response.status());
    }

    @Test
    void createTaskShouldSetCreatedAtAndUpdatedAt() {
        CreateTaskRequest request = createTaskRequest("title3");
        TaskResponse response = taskService.createTask(request);
        assertNotNull(response.createdAt());
        assertNotNull(response.updatedAt());
        assertEquals(response.createdAt(), response.updatedAt());
    }

    @Test
    void getTaskShouldReturnExistingTask() {
        CreateTaskRequest request = createTaskRequest("title4");
        TaskResponse response = taskService.createTask(request);
        TaskResponse responseGetTask = taskService.getTask(response.id());
        assertEquals(response.id(), responseGetTask.id());
        assertEquals(response.title(), responseGetTask.title());
    }

    @Test
    void getTaskShouldThrowTaskNotFoundException() {
        assertThrows(
                TaskNotFoundException.class,
                () -> taskService.getTask(Long.MAX_VALUE)
        );
    }

    @Test
    void getTasksShouldReturnAllTasks() {
        CreateTaskRequest request = createTaskRequest("title5");
        TaskResponse response = taskService.createTask(request);
        List<TaskResponse> responseGetTasks = taskService.getTasks();
        assertTrue(responseGetTasks.stream().map(TaskResponse::id).anyMatch(id -> Objects.equals(id, response.id())));
    }

    private CreateTaskRequest createTaskRequest(String title) {
        return new CreateTaskRequest(
                "reporter@test.com",
                "assignee@test.com",
                title,
                "description",
                LocalDate.now().plusDays(1)
        );
    }
}