package com.example.zerotrust.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resource")
public class ResourceController {

    @GetMapping("/public")
    public String publicResource() {
        return "This is a public resource (authenticated users only)";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminResource() {
        return "This is a restricted resource for ADMINs only";
    }

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public String userResource() {
        return "This is a restricted resource for USERs and ADMINs";
    }
}
