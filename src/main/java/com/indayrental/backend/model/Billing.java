package com.indayrental.backend.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "billing")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Billing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tenant_id", nullable = false)
    private Tenants tenant;

    @ManyToOne
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Column(name = "rent_amount", nullable = false)
    private Double rentAmount;

    @Column(name = "electricity_reading_img", nullable = false)
    private String electricityReadingImg; // Supabase public URL

    @Column(name = "previous_electricity_reading", nullable = false)
    private Double previousElectricityReading;

    @Column(name = "current_electricity_reading", nullable = false)
    private Double currentElectricityReading;

    @Column(name = "electricity_rate", nullable = false)
    private Double electricityRatePerKwh;

    @Column(name = "electricity_bill", nullable = false)
    private Double electricityBill;

    @Column(name = "water_bill", nullable = false)
    private Double waterBill;

    @Column(name = "total_amount", nullable = false)
    private Double totalAmount;

    @Column(name = "billing_date", nullable = false)
    private LocalDate billingDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(name = "status", nullable = false)
    private String status; // "UNPAID", "PAID", "OVERDUE"

    @Column(name = "date_paid")
    private LocalDate datePaid;
}