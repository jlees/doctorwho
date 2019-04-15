package com.lees.doctorwho.model;

import lombok.Data;

@Data
public class ApplicationUserModel {
    private String username;
    private String password;
    private String role;
}
