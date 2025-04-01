package com.taskmanager.service;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Task createTask(Task task, User user) {
        task.setUser(user);
        return taskRepository.save(task);
    }

    @Transactional(readOnly = true)
    public Page<Task> getUserTasks(User user, Pageable pageable) {
        return taskRepository.findByUser(user, pageable);
    }

    @Transactional(readOnly = true)
    public Task getTaskById(Long taskId) {
        return taskRepository.findById(taskId).orElse(null);
    }

    @Transactional
    public Task updateTask(Task task) {
        return taskRepository.save(task);
    }

    @Transactional
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksByStatus(User user, Task.TaskStatus status) {
        return taskRepository.findByUserAndStatus(user, status);
    }

    @Transactional(readOnly = true)
    public List<Task> getTasksBetweenDates(User user, Date startDate, Date endDate) {
        return taskRepository.findByUserAndDueDateBetween(user, startDate, endDate);
    }

    @Transactional(readOnly = true)
    public long countTasksByStatus(User user, Task.TaskStatus status) {
        return taskRepository.countByUserAndStatus(user, status);
    }
}