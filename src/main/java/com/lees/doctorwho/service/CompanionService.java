package com.lees.doctorwho.service;

import com.lees.doctorwho.entity.Companion;
import com.lees.doctorwho.entity.Doctor;
import com.lees.doctorwho.exception.EntityNotFoundException;
import com.lees.doctorwho.model.CompanionModel;
import com.lees.doctorwho.model.CompanionWithIdModel;
import com.lees.doctorwho.repository.CompanionRepository;
import com.lees.doctorwho.repository.DoctorRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class CompanionService {

    private CompanionRepository companionRepository;

    private DoctorRepository doctorRepository;

    public CompanionWithIdModel addCompanion(final CompanionModel companionModel) {
        Companion companion = new Companion();
        updateCompanionWithModel(companion, companionModel);
        return buildCompanionModel(companion);
    }

    public CompanionWithIdModel editCompanion(final long companionId, final CompanionModel companionModel) {
        Companion existingCompanion = findCompanionById(companionId);
        updateCompanionWithModel(existingCompanion, companionModel);
        return buildCompanionModel(existingCompanion);
    }

    private void updateCompanionWithModel(final Companion companion, final CompanionModel companionModel) {
        companion.setName(companionModel.getName());
        companion.setAvatarUrl(companionModel.getAvatarUrl());
        companion.setPhotoUrl(companionModel.getPhotoUrl());
        companion.setDescription(companionModel.getDescription());
        companion.removeAllDoctors();
        List<Doctor> doctors = doctorRepository.findAllById(companionModel.getDoctorIds());
        for (Doctor doctor : doctors) {
            companion.addDoctor(doctor);
        }
        companionRepository.save(companion);
    }

    public void deleteCompanion(final long companionId) {
        Companion companion = findCompanionById(companionId);
        companion.removeAllDoctors();
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

    private CompanionWithIdModel buildCompanionModel(final Companion companion) {
        CompanionWithIdModel companionModel = new CompanionWithIdModel();
        companionModel.setId(companion.getId());
        companionModel.setName(companion.getName());
        companionModel.setDescription(companion.getDescription());
        companionModel.setPhotoUrl(companion.getPhotoUrl());
        companionModel.setAvatarUrl(companion.getAvatarUrl());
        List<Doctor> doctors = companion.getDoctors();
        companionModel.setDoctorIds(doctors.stream().map(Doctor::getId).collect(Collectors.toSet()));
        return companionModel;
    }



}
