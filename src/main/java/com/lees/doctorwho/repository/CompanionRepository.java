package com.lees.doctorwho.repository;

import com.lees.doctorwho.entity.Companion;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CompanionRepository extends JpaRepository<Companion, Long> {
}
