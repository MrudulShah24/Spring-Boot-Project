package com.libb.booknest2.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.libb.booknest2.entities.Role;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
