package com.lees.doctorwho.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Entity
@Data
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    private String name;

    private int sortOrder;

    @ManyToMany
    @JoinTable(name = "doctor_companion_xref",
            joinColumns = { @JoinColumn(name = "doctor_id") },
            inverseJoinColumns = { @JoinColumn(name = "companion_id") })
    @Setter(AccessLevel.NONE)
    private List<Companion> companions = new ArrayList<>();

    public List<Companion> getCompanions() {
        return Collections.unmodifiableList(this.companions);
    }

    public void addCompanion(Companion companion) {
        if (companion != null) {
            this.companions.add(companion);
        }
    }

    public void removeCompanion(Companion companion) {
        if (companion != null) {
            this.companions.remove(companions);
        }
    }
}
