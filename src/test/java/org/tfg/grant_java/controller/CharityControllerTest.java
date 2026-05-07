package org.tfg.grant_java.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.tfg.grant_java.dto.CharityDTO;
import org.tfg.grant_java.service.CharityService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CharityController.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {CharityController.class, CharityDTO.class})
class CharityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CharityService charityService;

    @Test
    void getAllActiveCharities_shouldReturn200_andCharityList() throws Exception {
        CharityDTO charity1 = new CharityDTO();
        charity1.setId(1L);
        charity1.setName("Charity One");

        CharityDTO charity2 = new CharityDTO();
        charity2.setId(2L);
        charity2.setName("Charity Two");

        when(charityService.getAllActiveCharities()).thenReturn(List.of(charity1, charity2));

        mockMvc.perform(get("/app/charities")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Charity One"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Charity Two"));
    }
}