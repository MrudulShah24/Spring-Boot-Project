package com.libb.booknest2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.libb.booknest2.entities.Role;
import com.libb.booknest2.entities.User;
import com.libb.booknest2.repository.RoleRepository;
import com.libb.booknest2.repository.UserRepository;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User registerUser(User user, String roleName) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        
        // Normalize role name (ensure ROLE_ prefix)
        String normalizedRoleName = roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
        
        Role role = roleRepository.findByName(normalizedRoleName)
                .orElseThrow(() -> new RuntimeException("Role not found: " + normalizedRoleName));
        
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(role));
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
