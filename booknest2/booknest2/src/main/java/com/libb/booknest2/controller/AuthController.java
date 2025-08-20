package com.libb.booknest2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.libb.booknest2.entities.User;
import com.libb.booknest2.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register/member")
    public ResponseEntity<?> registerMember(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user, "MEMBER");
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/register/librarian")
    public ResponseEntity<?> registerLibrarian(@RequestBody User user) {
        try {
            User registeredUser = userService.registerUser(user, "LIBRARIAN");
            return ResponseEntity.ok(registeredUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}