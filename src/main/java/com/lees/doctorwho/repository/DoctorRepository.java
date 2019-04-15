package com.lees.doctorwho.repository;

import com.lees.doctorwho.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {
}
