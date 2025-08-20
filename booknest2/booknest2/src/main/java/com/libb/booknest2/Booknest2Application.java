package com.libb.booknest2;

import java.util.Collections;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.libb.booknest2.entities.Role;
import com.libb.booknest2.entities.User;
import com.libb.booknest2.repository.RoleRepository;
import com.libb.booknest2.repository.UserRepository;

@SpringBootApplication
public class Booknest2Application {

	public static void main(String[] args) {
		SpringApplication.run(Booknest2Application.class, args);
	}
	
	@Bean
	CommandLineRunner init(RoleRepository roleRepo, PasswordEncoder encoder, UserRepository userRepo) {
	    return args -> {
	        // Create default roles
	        if (roleRepo.findByName("ROLE_ADMIN").isEmpty()) {
	            roleRepo.save(new Role("ROLE_ADMIN"));
	        }
	        if (roleRepo.findByName("ROLE_LIBRARIAN").isEmpty()) {
	            roleRepo.save(new Role("ROLE_LIBRARIAN"));
	        }
	        if (roleRepo.findByName("ROLE_MEMBER").isEmpty()) {
	            roleRepo.save(new Role("ROLE_MEMBER"));
	        }

	        // Create initial admin if none exists
	        if (userRepo.findByUsername("admin").isEmpty()) {
	            User admin = new User();
	            admin.setUsername("admin");
	            admin.setPassword(encoder.encode("admin123"));
	            admin.setName("Admin User");
	            admin.setEmail("admin@library.com");
	            admin.setAddress("Library Admin Office");
	            
	            Role adminRole = roleRepo.findByName("ROLE_ADMIN")
	                .orElseThrow(() -> new RuntimeException("Admin role not found"));
	            admin.setRoles(Collections.singleton(adminRole));
	            
	            userRepo.save(admin);
	        }
	    };
	}

}
