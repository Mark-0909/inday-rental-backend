package com.indayrental.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Tenants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "room_id", nullable = false)
    private Long roomId;

    @Column(name = "move_in_date", nullable = false)
    private String moveInDate;

    @Column(name = "move_out_date", nullable = true)
    private String moveOutDate;

    @Column(name = "status", nullable = false)
    private String status;
}
