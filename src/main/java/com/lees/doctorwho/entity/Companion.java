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

    @ManyToMany(mappedBy="companions", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @Setter(AccessLevel.NONE)
    private List<Doctor> doctors = new ArrayList<>();

    public List<Doctor> getDoctors() {
        return Collections.unmodifiableList(this.doctors);
    }

    public void addDoctor(Doctor doctor) {
        if (doctor != null) {
            this.doctors.add(doctor);
            doctor.addCompanion(this);
        }
    }

    public void removeDoctor(Doctor doctor) {
        if (doctor != null) {
            this.doctors.remove(doctor);
            doctor.removeCompanion(this);
        }
    }

    public void removeAllDoctors() {
        for (Doctor doctor : this.doctors) {
            doctor.removeCompanion(this);
        }
        this.doctors.clear();
    }

}
