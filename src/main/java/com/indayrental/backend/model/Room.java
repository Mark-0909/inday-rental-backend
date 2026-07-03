package com.indayrental.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "room_number", nullable = false, unique = true)
    private String roomNumber;

    @Column(name = "status", nullable = false)
    private String status;

    @Column(name = "monthly_rent", nullable = false)
    private Double monthlyRent;

    @Column(name = "max_occupancy", nullable = false)
    private Integer maxOccupancy;

    @Column(name = "images", nullable = false)
    private String images;

    @Column(name = "description", nullable = true)
    private String description;

    @OneToOne
    @JoinColumn(name = "current_tenant_id", referencedColumnName = "id", nullable = true)
    private Tenants currentTenant;
}
