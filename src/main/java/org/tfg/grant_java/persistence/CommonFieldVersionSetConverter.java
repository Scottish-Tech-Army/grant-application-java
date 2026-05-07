package org.tfg.grant_java.persistence;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.tfg.grant_java.domain.CommonFieldVersionSet;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class CommonFieldVersionSetConverter implements AttributeConverter<CommonFieldVersionSet, String> {

    @Override
    public String convertToDatabaseColumn(CommonFieldVersionSet attribute) {
        try {
            if (attribute == null) return null;
            return JsonMapperHolder.mapper().writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Failed to serialize CommonFieldVersionSet to JSON", e);
        }
    }

    @Override
    public CommonFieldVersionSet convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isBlank()) return new CommonFieldVersionSet();
            return JsonMapperHolder.mapper().readValue(dbData, CommonFieldVersionSet.class);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to deserialize CommonFieldVersionSet from JSON", e);
        }
    }
}

