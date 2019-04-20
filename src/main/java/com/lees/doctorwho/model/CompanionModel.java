package com.lees.doctorwho.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Data
public class CompanionModel {

    @NotBlank(message="Name is required.")
    private String name;

    @NotBlank(message="Description is required.")
    private String description;

    @NotBlank(message="Photo Url is required.")
    private String photoUrl;

    @NotBlank(message="Avatar Url is required.")
    private String avatarUrl;

    @NotNull(message="Associated Doctors must not be null.")
    @Size(min=1, message="At least one Associated Doctor should exist.")
    private Set<Long> doctorIds = new HashSet<>();
}
