package org.study.task.service;

import org.springframework.stereotype.Service;
import org.study.task.api.CreateTaskRequest;
import org.study.task.api.TaskResponse;
import org.study.task.api.TaskStatus;
import org.study.task.domain.Task;
import org.study.task.exception.TaskNotFoundException;
import org.study.task.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public TaskResponse createTask(CreateTaskRequest request) {
        Task task = new Task(
                request.reporterEmail(),
                request.assigneeEmail(),
                request.title(),
                request.description(),
                request.dueDate());

        LocalDateTime now = LocalDateTime.now();
        task.setCreatedAt(now);
        task.setUpdatedAt(now);
        task.setStatus(TaskStatus.TODO);
        return toResponse(taskRepository.save(task));
    }

    public TaskResponse getTask(Long id) {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
        return toResponse(task);
    }

    public List<TaskResponse> getTasks() {
        return taskRepository.findAll().stream().map(this::toResponse).toList();
    }


    private TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getReporterEmail(),
                task.getAssigneeEmail(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getCreatedAt(),
                task.getUpdatedAt(),
                task.getClosedAt(),
                task.getDueDate()
        );
    }
}
