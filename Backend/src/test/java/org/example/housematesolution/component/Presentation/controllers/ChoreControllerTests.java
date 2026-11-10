package org.example.housematesolution.component.Presentation.controllers;

import org.example.housematesolution.Business.domain.Chore;
import org.example.housematesolution.Business.domain.House;
import org.example.housematesolution.Business.exceptions.ChoreAlreadyExistsException;
import org.example.housematesolution.Business.exceptions.HouseNotFoundException;
import org.example.housematesolution.Business.services.ChoreService;
import org.example.housematesolution.Presentation.controllers.ChoreController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChoreController.class)
class ChoreControllerTests {

    @Autowired
    private MockMvc mockMvc;

    // Fake service, so only the controller is tested
    @MockitoBean
    private ChoreService choreService;

    private final House house = new House(UUID.randomUUID(), "Student House", "ABC123");

    private final String validCreateRequest = """
            {"name": "Dishes", "intervalDays": 1, "effort": 2}
            """;

    // ---------- Create a chore ----------

    @Test
    void createChoreReturns201AndChore() throws Exception {
        Chore chore = new Chore(UUID.randomUUID(), "dishes", 1, 2, LocalDateTime.now(), house);
        when(choreService.createChore("Dishes", 1, 2, house.getId())).thenReturn(chore);

        mockMvc.perform(post("/houses/{houseId}/chores", house.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(chore.getId().toString()))
                .andExpect(jsonPath("$.name").value("dishes"))
                .andExpect(jsonPath("$.intervalDays").value(1))
                .andExpect(jsonPath("$.effort").value(2))
                .andExpect(jsonPath("$.createdAt").exists())
                // The house (and its join code) must not be sent back
                .andExpect(jsonPath("$.house").doesNotExist());
    }

    @Test
    void createChoreReturns409WhenChoreAlreadyExists() throws Exception {
        when(choreService.createChore("Dishes", 1, 2, house.getId()))
                .thenThrow(new ChoreAlreadyExistsException("dishes"));

        mockMvc.perform(post("/houses/{houseId}/chores", house.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateRequest))
                .andExpect(status().isConflict())
                .andExpect(content().string("Chore already exists: dishes"));
    }

    @Test
    void createChoreReturns404WhenHouseDoesNotExist() throws Exception {
        when(choreService.createChore("Dishes", 1, 2, house.getId()))
                .thenThrow(new HouseNotFoundException(house.getId()));

        mockMvc.perform(post("/houses/{houseId}/chores", house.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateRequest))
                .andExpect(status().isNotFound());
    }

    @Test
    void createChoreReturns400WhenNameIsBlank() throws Exception {
        String invalidRequest = """
                {"name": "", "intervalDays": 1, "effort": 2}
                """;

        mockMvc.perform(post("/houses/{houseId}/chores", house.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("name: must not be blank"));

        verify(choreService, never()).createChore(anyString(), anyInt(), anyInt(), any(UUID.class));
    }

    @Test
    void createChoreReturns400WhenEffortIsOutOfRange() throws Exception {
        String invalidRequest = """
                {"name": "Dishes", "intervalDays": 1, "effort": 4}
                """;

        mockMvc.perform(post("/houses/{houseId}/chores", house.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("effort: must be less than or equal to 3"));
    }

    @Test
    void createChoreReturns400WhenIntervalDaysIsZero() throws Exception {
        String invalidRequest = """
                {"name": "Dishes", "intervalDays": 0, "effort": 2}
                """;

        mockMvc.perform(post("/houses/{houseId}/chores", house.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("intervalDays: must be greater than or equal to 1"));
    }

    // ---------- Get chores ----------

    @Test
    void getChoresReturns200AndChores() throws Exception {
        when(choreService.getChores(house.getId())).thenReturn(
                List.of(
                        new Chore(UUID.randomUUID(), "dishes", 1, 2, LocalDateTime.now(), house),
                        new Chore(UUID.randomUUID(), "vacuum", 7, 3, LocalDateTime.now(), house)
                )
        );

        mockMvc.perform(get("/houses/{houseId}/chores", house.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].name").value("dishes"))
                .andExpect(jsonPath("$[1].name").value("vacuum"));
    }

    @Test
    void getChoresReturnsEmptyListWhenNoChores() throws Exception {
        when(choreService.getChores(house.getId())).thenReturn(List.of());

        mockMvc.perform(get("/houses/{houseId}/chores", house.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void getChoresReturns404WhenHouseDoesNotExist() throws Exception {
        when(choreService.getChores(house.getId()))
                .thenThrow(new HouseNotFoundException(house.getId()));

        mockMvc.perform(get("/houses/{houseId}/chores", house.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    void getChoresReturns400WhenHouseIdIsNotUuid() throws Exception {
        mockMvc.perform(get("/houses/{houseId}/chores", "not-a-uuid"))
                .andExpect(status().isBadRequest());
    }
}
