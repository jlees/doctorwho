package com.lees.doctorwho.repository;

import com.lees.doctorwho.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Long> {
}