package com.lees.doctorwho.service;

import com.lees.doctorwho.entity.Companion;
import com.lees.doctorwho.entity.Doctor;
import com.lees.doctorwho.exception.EntityNotFoundException;
import com.lees.doctorwho.model.CompanionModel;
import com.lees.doctorwho.model.CompanionWithIdModel;
import com.lees.doctorwho.repository.CompanionRepository;
import com.lees.doctorwho.repository.DoctorRepository;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class CompanionService {

    @Autowired
    private CompanionRepository companionRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    public void addCompanion(final CompanionModel companionModel) {
        Companion companion = new Companion();
        companion.setName(companionModel.getName());
        List<Doctor> doctors = doctorRepository.findAllById(companionModel.getDoctorIds());
        for (Doctor doctor : doctors) {
            companion.addDoctor(doctor);
        }
        companionRepository.save(companion);
    }

    public void editCompanion(final long companionId, final CompanionModel companionModel) {
        Companion existingCompanion = findCompanionById(companionId);
        existingCompanion.setName(companionModel.getName());
        existingCompanion.setAvatarUrl(companionModel.getAvatarUrl());
        existingCompanion.setPhotoUrl(companionModel.getPhotoUrl());
        existingCompanion.setDescription(companionModel.getDescription());
        existingCompanion.removeAllDoctors();
        List<Doctor> doctors = doctorRepository.findAllById(companionModel.getDoctorIds());
        for (Doctor doctor : doctors) {
            existingCompanion.addDoctor(doctor);
        }
        companionRepository.save(existingCompanion);
    }

    public void deleteCompanion(final long companionId) {
        Companion companion = findCompanionById(companionId);
        companionRepository.delete(companion);
    }

    public Set<CompanionWithIdModel> getCompanions() {
        Set<CompanionWithIdModel> companionModels = new HashSet<>();
        for (Companion companion : companionRepository.findAll()) {
            companionModels.add(buildCompanionModel(companion));
        }
        return companionModels;
    }

    public CompanionWithIdModel getCompanion(long companionId) {
        Companion companion = findCompanionById(companionId);
        return buildCompanionModel(companion);
    }

    private Companion findCompanionById(final long companionId) {
        Companion companion = companionRepository.findById(companionId).orElse(null);
        if (companion == null) {
            throw new EntityNotFoundException("Companion not found");
        }
        return companion;
    }

    public CompanionWithIdModel buildCompanionModel(final Companion companion) {
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



}
