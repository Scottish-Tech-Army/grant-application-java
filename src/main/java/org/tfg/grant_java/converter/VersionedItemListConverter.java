package org.tfg.grant_java.converter;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.tfg.grant_java.model.VersionedItem;

import java.util.ArrayList;
import java.util.List;

@Converter
public class VersionedItemListConverter implements AttributeConverter<List<VersionedItem>, String> {

    private static final ObjectMapper MAPPER = new ObjectMapper();
    private static final TypeReference<List<VersionedItem>> TYPE =
            new TypeReference<>() {
            };

    @Override
    public String convertToDatabaseColumn(List<VersionedItem> attribute) {
        try {
            return MAPPER.writeValueAsString(attribute == null ? new ArrayList<>() : attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize fields/questions to JSON", e);
        }
    }

    @Override
    public List<VersionedItem> convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) return new ArrayList<>();
            return MAPPER.readValue(dbData, TYPE);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize fields/questions JSON", e);
        }
    }
}
