package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    Page<Task> findByUser(User user, Pageable pageable);
    List<Task> findByUserAndStatus(User user, Task.TaskStatus status);
    List<Task> findByUserAndDueDateBetween(User user, Date startDate, Date endDate);
    long countByUserAndStatus(User user, Task.TaskStatus status);
}