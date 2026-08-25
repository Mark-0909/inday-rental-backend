package com.indayrental.backend.controller;

import com.indayrental.backend.dto.BillingRequestDTO;
import com.indayrental.backend.model.Billing;
import com.indayrental.backend.model.Room;
import com.indayrental.backend.model.Tenants;
import com.indayrental.backend.repository.BillingRepository;
import com.indayrental.backend.repository.RoomRepository;
import com.indayrental.backend.repository.TenantsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {

    @Autowired
    private BillingRepository billingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private TenantsRepository tenantsRepository;

    @GetMapping
    public List<Billing> getAllBilling() {
        return billingRepository.findAll();
    }

    // Only ONE @PostMapping method for creating invoices
    @PostMapping
    public Billing createBilling(@RequestBody BillingRequestDTO dto) {
        Billing billing = new Billing();

        Tenants tenant = tenantsRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found with ID: " + dto.getTenantId()));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + dto.getRoomId()));

        billing.setTenant(tenant);
        billing.setRoom(room);
        billing.setRentAmount(dto.getRentAmount());
        billing.setElectricityReadingImg(dto.getElectricityReadingImg());
        billing.setPreviousElectricityReading(dto.getPreviousElectricityReading());
        billing.setCurrentElectricityReading(dto.getCurrentElectricityReading());
        billing.setElectricityRatePerKwh(dto.getElectricityRatePerKwh());
        billing.setElectricityBill(dto.getElectricityBill());
        billing.setWaterBill(dto.getWaterBill());
        billing.setTotalAmount(dto.getTotalAmount());
        billing.setBillingDate(dto.getBillingDate());
        billing.setDueDate(dto.getDueDate());
        billing.setStatus(dto.getStatus());
        billing.setDatePaid(dto.getDatePaid());

        return billingRepository.save(billing);
    }

    @PutMapping("/{id}")
    public Billing updateBilling(@PathVariable Long id, @RequestBody BillingRequestDTO dto) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing not found with ID: " + id));

        Tenants tenant = tenantsRepository.findById(dto.getTenantId())
                .orElseThrow(() -> new RuntimeException("Tenant not found with ID: " + dto.getTenantId()));
        Room room = roomRepository.findById(dto.getRoomId())
                .orElseThrow(() -> new RuntimeException("Room not found with ID: " + dto.getRoomId()));

        billing.setTenant(tenant);
        billing.setRoom(room);
        billing.setRentAmount(dto.getRentAmount());
        billing.setElectricityReadingImg(dto.getElectricityReadingImg());
        billing.setPreviousElectricityReading(dto.getPreviousElectricityReading());
        billing.setCurrentElectricityReading(dto.getCurrentElectricityReading());
        billing.setElectricityRatePerKwh(dto.getElectricityRatePerKwh());
        billing.setElectricityBill(dto.getElectricityBill());
        billing.setWaterBill(dto.getWaterBill());
        billing.setTotalAmount(dto.getTotalAmount());
        billing.setBillingDate(dto.getBillingDate());
        billing.setDueDate(dto.getDueDate());
        billing.setStatus(dto.getStatus());
        billing.setDatePaid(dto.getDatePaid());

        return billingRepository.save(billing);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBilling(@PathVariable Long id) {
        Billing billing = billingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Billing not found with ID: " + id));

        billingRepository.delete(billing);
        return ResponseEntity.noContent().build();
    }
}