package com.lees.doctorwho.controller;

import com.lees.doctorwho.entity.Doctor;
import com.lees.doctorwho.model.DoctorWithIdModel;
import com.lees.doctorwho.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/doctors")
@AllArgsConstructor
public class DoctorController {

    private DoctorRepository doctorRepository;

    @PostMapping
    public void addDoctor(@RequestBody Doctor doctor) {
        doctorRepository.save(doctor);
    }

    @GetMapping
    public List<DoctorWithIdModel> getDoctors() {
        List<Doctor> doctors = doctorRepository.findAllByOrderBySortOrderAsc();
        return doctors.stream().map(doctor -> {
            DoctorWithIdModel model = new DoctorWithIdModel();
            model.setId(doctor.getId());
            model.setName(doctor.getName());
            return model;
        }).collect(Collectors.toList());
    }

    @PutMapping("/{id}")
    public void editDoctor(@PathVariable long id, @RequestBody Doctor doctor) {
        Doctor existingDoctor = doctorRepository.findById(id).get();
        Assert.notNull(existingDoctor, "Doctor not found");
        existingDoctor.setName(doctor.getName());
        doctorRepository.save(existingDoctor);
    }

    @DeleteMapping("/{id}")
    public void deleteDoctor(@PathVariable long id) {
        Doctor doctor = doctorRepository.findById(id).get();
        doctorRepository.delete(doctor);
    }
}
