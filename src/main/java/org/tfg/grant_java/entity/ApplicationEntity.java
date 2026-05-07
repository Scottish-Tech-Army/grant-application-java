package org.tfg.grant_java.entity;


import jakarta.persistence.*;
import lombok.*;
import org.tfg.grant_java.converter.VersionedItemListConverter;
import org.tfg.grant_java.model.VersionedItem;

import java.util.List;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    private String name;
    private String funderName;
    private String status;
    private String startDate;
    private String endDate;
    private String createDate;

    @Lob
    @Column(name = "fields_json", columnDefinition = "CLOB")
    @Convert(converter = VersionedItemListConverter.class)
    private List<VersionedItem> fields;

    @Lob
    @Column(name = "questions_json", columnDefinition = "CLOB")
    @Convert(converter = VersionedItemListConverter.class)
    private List<VersionedItem> questions;
}

