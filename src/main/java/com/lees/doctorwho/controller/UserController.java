package com.lees.doctorwho.controller;

import com.lees.doctorwho.entity.ApplicationUser;
import com.lees.doctorwho.entity.Role;
import com.lees.doctorwho.model.ApplicationUserModel;
import com.lees.doctorwho.repository.ApplicationUserRepository;
import com.lees.doctorwho.repository.RoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/users")
@AllArgsConstructor
public class UserController {

    private ApplicationUserRepository applicationUserRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping("/sign-up")
    public void signUp(@RequestBody ApplicationUserModel user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        ApplicationUser applicationUser = new ApplicationUser();
        applicationUser.setUsername(user.getUsername());
        applicationUser.setPassword(user.getPassword());
        Role role = roleRepository.findRoleByName(user.getRole());
        if (role == null) {
            role = roleRepository.findRoleByName(Role.USER);
        }
        applicationUser.getRoles().add(role);
        role.getUsers().add(applicationUser);
        applicationUserRepository.save(applicationUser);
    }
}