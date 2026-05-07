package com.commongrant.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "organizations")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor
public class Organization {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String ein;
    private String missionStatement;
    private BigDecimal annualBudget;
    private String execDirectorName;
    private String execDirectorEmail;
    private String phone;
    private String city;
    private String state;

    @Enumerated(EnumType.STRING)
    private TaxStatus taxStatus;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<GrantApplication> applications = new ArrayList<>();

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<VaultDocument> documents = new ArrayList<>();

    public enum TaxStatus {
        NONPROFIT_501C3, NONPROFIT_501C4, FISCAL_SPONSOR
    }
}

