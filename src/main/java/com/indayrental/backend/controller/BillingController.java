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

import java.time.LocalDate;
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
        
        Double rentAmount = dto.getRentAmount() != null ? dto.getRentAmount() : 0.0;
        Double previousReading = dto.getPreviousElectricityReading() != null ? dto.getPreviousElectricityReading() : 0.0;
        Double currentReading = dto.getCurrentElectricityReading() != null ? dto.getCurrentElectricityReading() : 0.0;
        Double rate = dto.getElectricityRatePerKwh() != null ? dto.getElectricityRatePerKwh() : 0.0;
        Double waterBill = dto.getWaterBill() != null ? dto.getWaterBill() : 0.0;
        
        Double electricityBill = dto.getElectricityBill();
        if (electricityBill == null) {
            electricityBill = (currentReading - previousReading) * rate;
        }
        
        Double totalAmount = dto.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = rentAmount + electricityBill + waterBill;
        }

        billing.setRentAmount(rentAmount);
        billing.setElectricityReadingImg(dto.getElectricityReadingImg() != null ? dto.getElectricityReadingImg() : "");
        billing.setPreviousElectricityReading(previousReading);
        billing.setCurrentElectricityReading(currentReading);
        billing.setElectricityRatePerKwh(rate);
        billing.setElectricityBill(electricityBill);
        billing.setWaterBill(waterBill);
        billing.setTotalAmount(totalAmount);
        billing.setBillingDate(dto.getBillingDate() != null ? dto.getBillingDate() : LocalDate.now());
        billing.setDueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(7));
        billing.setStatus(dto.getStatus() != null ? dto.getStatus() : "UNPAID");
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

        Double rentAmount = dto.getRentAmount() != null ? dto.getRentAmount() : 0.0;
        Double previousReading = dto.getPreviousElectricityReading() != null ? dto.getPreviousElectricityReading() : 0.0;
        Double currentReading = dto.getCurrentElectricityReading() != null ? dto.getCurrentElectricityReading() : 0.0;
        Double rate = dto.getElectricityRatePerKwh() != null ? dto.getElectricityRatePerKwh() : 0.0;
        Double waterBill = dto.getWaterBill() != null ? dto.getWaterBill() : 0.0;

        Double electricityBill = dto.getElectricityBill();
        if (electricityBill == null) {
            electricityBill = (currentReading - previousReading) * rate;
        }

        Double totalAmount = dto.getTotalAmount();
        if (totalAmount == null) {
            totalAmount = rentAmount + electricityBill + waterBill;
        }

        billing.setRentAmount(rentAmount);
        billing.setElectricityReadingImg(dto.getElectricityReadingImg() != null ? dto.getElectricityReadingImg() : "");
        billing.setPreviousElectricityReading(previousReading);
        billing.setCurrentElectricityReading(currentReading);
        billing.setElectricityRatePerKwh(rate);
        billing.setElectricityBill(electricityBill);
        billing.setWaterBill(waterBill);
        billing.setTotalAmount(totalAmount);
        billing.setBillingDate(dto.getBillingDate() != null ? dto.getBillingDate() : LocalDate.now());
        billing.setDueDate(dto.getDueDate() != null ? dto.getDueDate() : LocalDate.now().plusDays(7));
        billing.setStatus(dto.getStatus() != null ? dto.getStatus() : "UNPAID");
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