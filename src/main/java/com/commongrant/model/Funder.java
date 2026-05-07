package com.commongrant.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "funders")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Funder {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private BigDecimal minGrant;
    private BigDecimal maxGrant;
    private String focusAreas;      // comma-separated
    private String geographicFocus;
    private LocalDate nextDeadline;
    private boolean active;
}

