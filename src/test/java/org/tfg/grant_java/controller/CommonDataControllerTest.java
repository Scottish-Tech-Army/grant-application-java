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
import org.tfg.grant_java.dto.CommonDataDTO;
import org.tfg.grant_java.service.CommonDataService;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CommonDataController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {CommonDataController.class, CommonDataDTO.class})
class CommonDataControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommonDataService commonDataService;

    @Test
    void getCommonData_shouldReturn200_andCommonData() throws Exception {
        Long id = 1L;

        CommonDataDTO response = new CommonDataDTO();
        response.setId(id);
        response.setCharityId(1L);
        response.setVersion("1.0");
        response.setDataJson("{\"key\":\"value\"}");
        response.setTemplateTitle("Test Template");
        response.setDescription("Test Description");
        response.setIsActive(true);
        response.setCreatedAt(LocalDateTime.now());

        when(commonDataService.getCommonDataById(id)).thenReturn(response);

        mockMvc.perform(get("/app/common-data/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.charityId").value(1L))
                .andExpect(jsonPath("$.version").value("1.0"))
                .andExpect(jsonPath("$.dataJson").value("{\"key\":\"value\"}"))
                .andExpect(jsonPath("$.templateTitle").value("Test Template"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.isActive").value(true));
    }

    @Test
    void getAllCommonDataByCharityId_shouldReturn200_andCommonDataList() throws Exception {
        Long charityId = 1L;

        CommonDataDTO data1 = new CommonDataDTO();
        data1.setId(1L);
        data1.setCharityId(charityId);
        data1.setVersion("1.0");

        CommonDataDTO data2 = new CommonDataDTO();
        data2.setId(2L);
        data2.setCharityId(charityId);
        data2.setVersion("1.1");

        when(commonDataService.getAllCommonDataByCharityId(charityId)).thenReturn(List.of(data1, data2));

        mockMvc.perform(get("/app/common-data/charity/{id}", charityId))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].charityId").value(charityId))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].charityId").value(charityId));
    }

    @Test
    void saveCommonData_shouldReturn201_andSavedDataId() throws Exception {
        CommonDataDTO request = new CommonDataDTO();
        request.setCharityId(1L);
        request.setVersion("1.0");
        request.setDataJson("{\"key\":\"value\"}");
        request.setTemplateTitle("New Template");
        request.setDescription("New Description");
        request.setIsActive(true);

        Long savedId = 1L;

        when(commonDataService.saveCommonData(any(CommonDataDTO.class))).thenReturn(savedId);

        mockMvc.perform(post("/app/common-data")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().string(savedId.toString()));
    }
}