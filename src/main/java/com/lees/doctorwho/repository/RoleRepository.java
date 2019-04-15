package com.lees.doctorwho.repository;

import com.lees.doctorwho.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;


public interface RoleRepository extends JpaRepository<Role, Long> {

    Role findRoleByName(final String roleName);
}
