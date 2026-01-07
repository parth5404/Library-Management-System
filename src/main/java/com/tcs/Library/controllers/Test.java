package com.tcs.Library.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Test {

    @GetMapping("/whoami")
    public ResponseEntity<?> whoami(
            org.springframework.security.core.Authentication authentication) {
        if (authentication == null)
            return ResponseEntity.ok("AUTH = null");
        return ResponseEntity.ok("name=" + authentication.getName() + ", authenticated="
                + authentication.isAuthenticated() + ", authorities="
                + authentication.getAuthorities());
    }


}
