package com.lees.doctorwho.entity;

import com.google.common.collect.Lists;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.CascadeType;
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
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String name;

    private String code;

    private int sortOrder;

    @Setter(AccessLevel.NONE)
    @OneToMany(mappedBy = "doctor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DoctorCompanionXref> doctorCompanionXrefList = new ArrayList<>();

    public List<Companion> getCompanions() {
        Set<Companion> companions = new HashSet<>();
        for (DoctorCompanionXref doctorCompanionXref : this.doctorCompanionXrefList) {
            companions.add(doctorCompanionXref.getCompanion());
        }
        return Collections.unmodifiableList(Lists.newArrayList(companions));
    }

    public void addCompanion(Companion companion) {
        if (companion != null) {
            this.doctorCompanionXrefList.add(new DoctorCompanionXref(this, companion));
        }
    }

    public void removeCompanion(Companion companion) {
        if (companion != null && this.doctorCompanionXrefList != null) {
            DoctorCompanionXref matchingDoctorCompanionXref = null;
            for (DoctorCompanionXref doctorCompanionXref : this.doctorCompanionXrefList) {
                if (companion.getId() == doctorCompanionXref.getCompanion().getId()) {
                    matchingDoctorCompanionXref = doctorCompanionXref;
                    break;
                }
            }
            if (matchingDoctorCompanionXref != null) {
                this.doctorCompanionXrefList.remove(matchingDoctorCompanionXref);
            }
        }
    }
}
