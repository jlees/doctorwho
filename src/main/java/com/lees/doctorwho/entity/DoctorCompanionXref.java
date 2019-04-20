package com.lees.doctorwho.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Data
@NoArgsConstructor
public class DoctorCompanionXref implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private long id;

    @ManyToOne(optional = false)
    private Doctor doctor;

    @ManyToOne(optional = false)
    private Companion companion;

    public DoctorCompanionXref(Doctor doctor, Companion companion) {
        this.doctor = doctor;
        this.companion = companion;
    }

    @Override
    public String toString() {
        return "DoctorCompanionXref{" +
                "doctorId=" + (doctor == null ? "" : doctor.getId()) +
                ", companionId=" + (companion == null ? "" : companion.getId()) +
                '}';
    }
}
