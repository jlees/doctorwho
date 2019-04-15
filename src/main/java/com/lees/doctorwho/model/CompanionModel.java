package com.lees.doctorwho.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class CompanionModel {
    private String name;
    private Set<Long> doctorIds = new HashSet<>();
}
