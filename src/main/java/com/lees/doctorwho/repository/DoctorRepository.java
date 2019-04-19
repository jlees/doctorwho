package com.lees.doctorwho.repository;

import com.lees.doctorwho.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    List<Doctor> findAllByOrderBySortOrderAsc();
}
