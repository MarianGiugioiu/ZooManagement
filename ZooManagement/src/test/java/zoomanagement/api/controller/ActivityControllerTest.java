package zoomanagement.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.exception.EmployeeBusyException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.ActivityService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static zoomanagement.api.util.MockDataUtils.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static zoomanagement.api.util.MockDataUtils.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ActivityController.class)
class ActivityControllerTest {

    @MockBean
    private ActivityService activityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void addActivityStatusCreated() throws Exception{
        ActivityDTO activityDTO = anActivityDTO("Cleaning BearPublic",
                new ArrayList<>(Arrays.asList("Alin")),
                "cleaning",
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30),
                "BearPublic"
        );
        Employee employee = anEmployee("Alin");
        Pen pen = aPen("BearPublic");

        Activity activity = Activity.builder()
                .name(activityDTO.getName())
                .action(activityDTO.getAction())
                .status("not started")
                .startTime(activityDTO.getStartTime())
                .endTime(activityDTO.getEndTime())
                .pen(pen)
                .employees(new ArrayList<>(Arrays.asList(employee)))
                .build();

        when(activityService.add(activityDTO)).thenReturn(activity);

        mockMvc.perform(post("/api/activities")
                .content(objectMapper.writeValueAsString(activityDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(activity.getName())))
                .andExpect(jsonPath("$.employees[0].name", is(employee.getName())))
                .andExpect(jsonPath("$.pen.name", is(pen.getName())))
                .andExpect(jsonPath("startTime", is(activityDTO.getStartTime().toString() + ":00")));
    }

    @Test
    void addActivityEmployeeNotFound() throws Exception{
        ActivityDTO activityDTO = anActivityDTO("Cleaning BearPublic",
                new ArrayList<>(Arrays.asList("Alin")),
                "cleaning",
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30),
                "BearPublic"
        );

        when(activityService.add(activityDTO)).thenThrow(new ResourceNotFoundException("Method add: Employee not found."));

        mockMvc.perform(post("/api/activities")
                .content(objectMapper.writeValueAsString(activityDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method add: Employee not found.")));
    }

    @Test
    void addActivityEmployeeBusy() throws Exception{
        ActivityDTO activityDTO = anActivityDTO("Cleaning BearPublic",
                new ArrayList<>(Arrays.asList("Alin")),
                "cleaning",
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30),
                "BearPublic"
        );

        when(activityService.add(activityDTO)).thenThrow(new EmployeeBusyException("Method add: Employee is busy with another activity."));

        mockMvc.perform(post("/api/activities")
                .content(objectMapper.writeValueAsString(activityDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message", is("Method add: Employee is busy with another activity.")));
    }

    @Test
    void addActivityPenNotFound() throws Exception{
        ActivityDTO activityDTO = anActivityDTO("Cleaning BearPublic",
                new ArrayList<>(Arrays.asList("Alin")),
                "cleaning",
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30),
                "BearPublic"
        );

        when(activityService.add(activityDTO)).thenThrow(new ResourceNotFoundException("Method add: Pen not found."));

        mockMvc.perform(post("/api/activities")
                .content(objectMapper.writeValueAsString(activityDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method add: Pen not found.")));
    }
}