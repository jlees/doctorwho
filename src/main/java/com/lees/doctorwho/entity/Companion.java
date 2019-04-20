package com.lees.doctorwho.entity;


import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Entity
@Data
public class Companion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String name;

    private String description;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @ToString.Exclude
    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "companion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DoctorCompanionXref> doctorCompanionXrefList = new ArrayList<>();

    public List<Doctor> getDoctors() {
        Set<Doctor> doctors = new HashSet<>();
        for (DoctorCompanionXref doctorCompanionXref : this.doctorCompanionXrefList) {
            doctors.add(doctorCompanionXref.getDoctor());
        }
        return Collections.unmodifiableList(Lists.newArrayList(doctors));
    }

    private DoctorCompanionXref findDoctorCompanionXrefByDoctor(final Doctor doctor) {
        if (doctor != null && this.doctorCompanionXrefList != null) {
            DoctorCompanionXref matchingDoctorCompanionXref = null;
            for (DoctorCompanionXref doctorCompanionXref : this.doctorCompanionXrefList) {
                if (doctor.getId() == doctorCompanionXref.getDoctor().getId()) {
                    return doctorCompanionXref;
                }
            }
        }
        return null;
    }

    public void addDoctor(final Doctor doctor) {
        if (doctor != null && findDoctorCompanionXrefByDoctor(doctor) == null) {
            this.doctorCompanionXrefList.add(new DoctorCompanionXref(doctor, this));
        }
    }

    public void removeDoctor(final Doctor doctor) {
        if (doctor != null && this.doctorCompanionXrefList != null) {
            DoctorCompanionXref matchingDoctorCompanionXref = findDoctorCompanionXrefByDoctor(doctor);
            if (matchingDoctorCompanionXref != null) {
                this.doctorCompanionXrefList.remove(matchingDoctorCompanionXref);
            }
        }
    }

    public void removeAllDoctors() {
        DoctorCompanionXref[] doctorCompanionXrefs = this.doctorCompanionXrefList.toArray(new DoctorCompanionXref[0]);
        for (DoctorCompanionXref doctorCompanionXref : doctorCompanionXrefs) {
            doctorCompanionXref.getDoctor().removeCompanion(doctorCompanionXref.getCompanion());
            this.doctorCompanionXrefList.remove(doctorCompanionXrefs);
        }
    }

}
