package com.indayrental.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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

    @Lob
    @Convert(converter = StringListJsonConverter.class)
    @Column(name = "images", nullable = false, columnDefinition = "LONGTEXT")
    @Builder.Default
    private List<String> images = new ArrayList<>();

    @Column(name = "description", nullable = true)
    private String description;

    @OneToOne
    @JoinColumn(name = "current_tenant_id", referencedColumnName = "id", nullable = true)
    private Tenants currentTenant;
}
