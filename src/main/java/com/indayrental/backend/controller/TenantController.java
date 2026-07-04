package com.indayrental.backend.controller;

import com.indayrental.backend.model.Tenants;
import com.indayrental.backend.repository.TenantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class TenantController {
    @Autowired
    private TenantsRepository tenantRepository;

    @GetMapping
    public List<Tenants> getAlltenants() {
        return tenantRepository.findAll();
    }

    @PostMapping
    public Tenants registerTenant(@RequestBody Tenants tenant) {
        return tenantRepository.save(tenant);
    }

}
