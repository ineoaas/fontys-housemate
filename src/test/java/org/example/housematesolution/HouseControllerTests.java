package org.example.housematesolution;

import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.domain.User;
import org.example.housematesolution.Business.exceptions.EmailAlreadyUsedException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HouseController.class)
class HouseControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Fake service, so only the controller is tested
    @MockitoBean
    private HouseService houseService;

    private final String validRequest = """
            {"joinCode": "ABC123", "name": "Inci", "email": "inci@example.com"}
            """;

    @Test
    void joinHouseReturnsUser() throws Exception {
        House house = new House(UUID.randomUUID(), "Student House", "ABC123");
        User user = new User(UUID.randomUUID(), "Inci", "inci@example.com", house);
        when(houseService.joinHouse("ABC123", "Inci", "inci@example.com")).thenReturn(user);

        mockMvc.perform(post("/houses/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId().toString()))
                .andExpect(jsonPath("$.name").value("Inci"))
                .andExpect(jsonPath("$.houseId").value(house.getId().toString()))
                .andExpect(jsonPath("$.houseName").value("Student House"));
    }

    @Test
    void joinHouseReturns404WhenJoinCodeIsWrong() throws Exception {
        when(houseService.joinHouse("ABC123", "Inci", "inci@example.com"))
                .thenThrow(new HouseNotFoundException("ABC123"));

        mockMvc.perform(post("/houses/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void joinHouseReturns409WhenEmailIsAlreadyUsed() throws Exception {
        when(houseService.joinHouse("ABC123", "Inci", "inci@example.com"))
                .thenThrow(new EmailAlreadyUsedException("inci@example.com"));

        mockMvc.perform(post("/houses/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validRequest))
                .andExpect(status().isConflict());
    }

    @Test
    void joinHouseReturns400WhenRequestIsInvalid() throws Exception {
        String invalidRequest = """
                {"joinCode": "", "name": "Inci", "email": "not-an-email"}
                """;

        mockMvc.perform(post("/houses/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest());
    }
}
