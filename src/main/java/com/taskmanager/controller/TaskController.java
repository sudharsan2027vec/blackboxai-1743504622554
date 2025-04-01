
package com.taskmanager.controller;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.repository.UserRepository;
import com.taskmanager.service.TaskService;
import com.taskmanager.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;
    
    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Page<Task> getUserTasks(Pageable pageable) {
        User user = getCurrentUser();
        return taskService.getUserTasks(user, pageable);
    }

    @PostMapping
    public Task createTask(@RequestBody Task task) {
        User user = getCurrentUser();
        return taskService.createTask(task, user);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTask(@PathVariable Long id) {
        Task task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task);
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable Long id, @RequestBody Task task) {
        Task existingTask = taskService.getTaskById(id);
        if (existingTask != null) {
            task.setId(id);
            task.setUser(existingTask.getUser());
            return ResponseEntity.ok(taskService.updateTask(task));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<?> getTasksByStatus(@PathVariable String status) {
        User user = getCurrentUser();
        try {
            Task.TaskStatus taskStatus = Task.TaskStatus.valueOf(status.toUpperCase());
            return ResponseEntity.ok(taskService.getTasksByStatus(user, taskStatus));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status value");
        }
    }

    @GetMapping("/due-date")
    public ResponseEntity<?> getTasksBetweenDates(
            @RequestParam String start,
            @RequestParam String end) {
        User user = getCurrentUser();
        try {
            Date startDate = new Date(Long.parseLong(start));
            Date endDate = new Date(Long.parseLong(end));
            return ResponseEntity.ok(taskService.getTasksBetweenDates(user, startDate, endDate));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getTaskStats() {
        User user = getCurrentUser();
        return ResponseEntity.ok(new Object() {
            public final long total = taskService.getUserTasks(user, Pageable.unpaged()).getTotalElements();
            public final long todo = taskService.countTasksByStatus(user, Task.TaskStatus.TODO);
            public final long inProgress = taskService.countTasksByStatus(user, Task.TaskStatus.IN_PROGRESS);
            public final long completed = taskService.countTasksByStatus(user, Task.TaskStatus.COMPLETED);
        });
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepository.findById(userDetails.getId())
            .orElseThrow(() -> new RuntimeException("User not found"));
    }
}