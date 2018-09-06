package com.example.demo.repository;

import com.example.demo.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepository extends CrudRepository<Role, Long> {
    Role findByLabel(String role_user);
}
