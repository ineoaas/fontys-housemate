package org.example.housematesolution.component.Presentation.controllers;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.services.HouseService;
import org.example.housematesolution.Presentation.controllers.HouseController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseController.class)
class HouseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Fake service, so only the controller is tested
    @MockitoBean
    private HouseService houseService;

    private final String validCreateRequest = """
            {"houseName": "Student House", "name": "Inci", "email": "inci@example.com"}
            """;

    // ---------- Create a house ----------

    @Test
    void createHouseReturns201AndUser() throws Exception {
        House house = new House(UUID.randomUUID(), "Student House", "ABC123");
        User creator = new User(UUID.randomUUID(), "Inci", "inci@example.com", house);
        when(houseService.createHouse("Student House", "Inci", "inci@example.com")).thenReturn(creator);

        mockMvc.perform(post("/houses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Inci"))
                .andExpect(jsonPath("$.houseName").value("Student House"))
                .andExpect(jsonPath("$.joinCode").value("ABC123"));
    }

    @Test
    void createHouseReturns409WhenEmailIsAlreadyUsed() throws Exception {
        when(houseService.createHouse("Student House", "Inci", "inci@example.com"))
                .thenThrow(new EmailAlreadyUsedException("inci@example.com"));

        mockMvc.perform(post("/houses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateRequest))
                .andExpect(status().isConflict());
    }

    @Test
    void createHouseReturns400WhenRequestIsInvalid() throws Exception {
        String invalidRequest = """
                {"houseName": "", "name": "Inci", "email": "inci@example.com"}
                """;

        mockMvc.perform(post("/houses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("houseName: must not be blank"));
    }
}
