package org.tfg.grant_java.persistence;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonMapperHolder {
    private static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    private JsonMapperHolder() {}

    public static ObjectMapper mapper() {
        return MAPPER;
    }
}

