package org.tfg.grant_java.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.tfg.grant_java.dto.ApplicationDTO;
import org.tfg.grant_java.model.ApplicationJsonDataResponse;
import org.tfg.grant_java.service.ApplicationService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ApplicationController.class)
@ContextConfiguration(classes = {ApplicationController.class, ApplicationJsonDataResponse.class})
@AutoConfigureMockMvc(addFilters = false) // avoids 403 if Spring Security is on classpath
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ApplicationService applicationService;

    @Test
    void getApplication_shouldReturn200_andBodyFields() throws Exception {
        // Arrange
        Long id = 10L;

        ApplicationJsonDataResponse response = new ApplicationJsonDataResponse();
        response.setCharityId(5L);
        response.setVersion("v1");
        response.setTemplateTitle("Student Grant Template");
        response.setDataJson("{\"a\":1}");
        response.setApplicationDataJson("{\"b\":2}");

        when(applicationService.getApplication(id)).thenReturn(response);

        // Act + Assert
        mockMvc.perform(get("/app/applications/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))

                // JSON field-level asserts (based on your response class)
                .andExpect(jsonPath("$.charityId").value(5))
                .andExpect(jsonPath("$.version").value("v1"))
                .andExpect(jsonPath("$.templateTitle").value("Student Grant Template"))
                .andExpect(jsonPath("$.dataJson").value("{\"a\":1}"))
                .andExpect(jsonPath("$.applicationDataJson").value("{\"b\":2}"));
    }

    @Test
    void getApplicationsByCharity_shouldReturn200_andJsonArray() throws Exception {
        Long charityId = 5L;

        when(applicationService.getApplicationsByCharity(charityId)).thenReturn(List.of());

        mockMvc.perform(get("/app/applications")
                        .param("charityId", charityId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

}