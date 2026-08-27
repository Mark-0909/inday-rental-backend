package com.indayrental.backend.controller;

import com.indayrental.backend.model.Tenants;
import com.indayrental.backend.repository.TenantsRepository;
import com.indayrental.backend.repository.RoomRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "*")
public class TenantController {

    @Autowired
    private TenantsRepository tenantRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping
    public List<Tenants> getAlltenants() {
        return tenantRepository.findAll();
    }

    @PostMapping
    public Tenants registerTenant(@RequestBody Tenants tenant) {
        return tenantRepository.save(tenant);
    }

    @PutMapping("/{id}")
    public Tenants updateTenant(@PathVariable Long id, @RequestBody Tenants tenantDetails) {
        Tenants tenant = tenantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tenant not found with id " + id));

        Long previousRoomId = tenant.getRoomId();

        tenant.setFullName(tenantDetails.getFullName());
        tenant.setPhone(tenantDetails.getPhone());
        tenant.setRoomId(tenantDetails.getRoomId());
        tenant.setMoveInDate(tenantDetails.getMoveInDate());
        tenant.setMoveOutDate(tenantDetails.getMoveOutDate());
        tenant.setBillingDate(tenantDetails.getBillingDate());
        tenant.setStatus(tenantDetails.getStatus());

        Tenants savedTenant = tenantRepository.save(tenant);

        // 1. If moved out or inactive, mark the previous room AVAILABLE
        if ("INACTIVE".equalsIgnoreCase(savedTenant.getStatus()) && previousRoomId != null) {
            roomRepository.findById(previousRoomId).ifPresent(r -> {
                r.setStatus("AVAILABLE");
                roomRepository.save(r);
            });
        }

        // 2. If active and assigned a room, mark the new room OCCUPIED
        if ("ACTIVE".equalsIgnoreCase(savedTenant.getStatus()) && savedTenant.getRoomId() != null) {
            roomRepository.findById(savedTenant.getRoomId()).ifPresent(r -> {
                r.setStatus("OCCUPIED");
                roomRepository.save(r);
            });
        }

        return savedTenant;
    }

}


