package org.tfg.grant_java.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tfg.grant_java.converter.VersionedItemListConverter;
import org.tfg.grant_java.model.VersionedItem;

import java.util.List;


@Entity
@Table(name = "user_predefined_fields")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserPredefinedFieldsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;


    @OneToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;


    private String createdDate;

    @Lob
    @Column(name = "fields_json", columnDefinition = "CLOB")
    @Convert(converter = VersionedItemListConverter.class)
    private List<VersionedItem> fields;
}


