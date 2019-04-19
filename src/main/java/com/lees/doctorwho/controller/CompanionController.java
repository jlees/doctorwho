package com.lees.doctorwho.controller;

import com.lees.doctorwho.entity.Companion;
import com.lees.doctorwho.entity.Doctor;
import com.lees.doctorwho.model.CompanionModel;
import com.lees.doctorwho.model.CompanionWithIdModel;
import com.lees.doctorwho.repository.CompanionRepository;
import com.lees.doctorwho.repository.DoctorRepository;
import io.swagger.annotations.ApiParam;
import lombok.AllArgsConstructor;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/companions")
@AllArgsConstructor
public class CompanionController {

    private CompanionRepository companionRepository;
    private DoctorRepository doctorRepository;

    @PostMapping
    public void addCompanion(@ApiParam(name="companion") @RequestBody CompanionModel companionModel) {
        Companion companion = new Companion();
        companion.setName(companionModel.getName());
        List<Doctor> doctors = doctorRepository.findAllById(companionModel.getDoctorIds());
         for (Doctor doctor : doctors) {
            companion.addDoctor(doctor);
        }
        companionRepository.save(companion);
    }

    private CompanionWithIdModel buildCompanionModel(final Companion companion) {
        CompanionWithIdModel companionModel = new CompanionWithIdModel();
        companionModel.setId(companion.getId());
        companionModel.setName(companion.getName());
        companionModel.setDescription(companion.getDescription());
        companionModel.setPhotoUrl(companion.getPhotoUrl());
        companionModel.setAvatarUrl(companion.getAvatarUrl());
        List<Doctor> doctors = companion.getDoctors();
        companionModel.setDoctorIds(doctors.stream().map(d -> d.getId()).collect(Collectors.toSet()));
        return companionModel;
    }

    @GetMapping
    public Set<CompanionWithIdModel> getCompanions() {
        Set<CompanionWithIdModel> companionModels = new HashSet<>();
        for (Companion companion : companionRepository.findAll()) {
            companionModels.add(buildCompanionModel(companion));
        }
        return companionModels;
    }

    @GetMapping("/{id}")
    public CompanionWithIdModel getCompanion(@PathVariable long id) {
        return buildCompanionModel(companionRepository.findById(id).get());
    }

    @PutMapping("/{id}")
    public void editCompanion(@PathVariable long id,
                              @ApiParam(name="companion") @RequestBody CompanionModel companionModel) {
        Companion existingCompanion = companionRepository.findById(id).get();
        Assert.notNull(existingCompanion, "Companion not found");
        existingCompanion.setName(companionModel.getName());
        existingCompanion.removeAllDoctors();
        List<Doctor> doctors = doctorRepository.findAllById(companionModel.getDoctorIds());
        for (Doctor doctor : doctors) {
            existingCompanion.addDoctor(doctor);
        }
        companionRepository.save(existingCompanion);
    }

    @DeleteMapping("/{id}")
    public void deleteCompanion(@PathVariable long id) {
        Companion companion = companionRepository.findById(id).get();
        companionRepository.delete(companion);
    }
}
