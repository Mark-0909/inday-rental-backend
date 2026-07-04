package com.indayrental.backend.controller;

import com.indayrental.backend.model.Billing;
import com.indayrental.backend.repository.BillingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/billing")
@CrossOrigin(origins = "*")
public class BillingController {
    @Autowired
    private BillingRepository billingRepository;

    @GetMapping
    public List<Billing> getAllBilling() {
        return billingRepository.findAll();
    }

    @PostMapping
    public Billing addBilling(@RequestBody Billing billing) {
        return billingRepository.save(billing);
    }
}
