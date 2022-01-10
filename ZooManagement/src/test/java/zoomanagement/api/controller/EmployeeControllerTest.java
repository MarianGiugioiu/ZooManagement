package zoomanagement.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zoomanagement.api.DTO.SpecializationBetweenHours;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.EmployeeService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static zoomanagement.api.util.MockDataUtils.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllEmployeesWithSpecializationWithoutIntervals() throws Exception{
        Species species = aSpecies("Bear");
        List<Species> speciesList = new ArrayList<>();
        speciesList.add(species);
        Employee employee = anEmployee("Andrei");
        employee.setSpecializations(speciesList);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        SpecializationBetweenHours specializationBetweenHours = new SpecializationBetweenHours("Bear",null, null);

        when(employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours)).thenReturn(employees);

        mockMvc.perform(get("/api/employees/specialization")
                .content(objectMapper.writeValueAsString(specializationBetweenHours))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Andrei")))
                .andExpect(jsonPath("$[0].specializations[0].name", is(species.getName())));
    }

    @Test
    void getAllEmployeesWithSpecializationWithIntervals() throws Exception{
        LocalDateTime startTime = LocalDateTime.of(2022, 01, 8, 13, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 01, 8, 15, 30);
        Species species = aSpecies("Bear");
        List<Species> speciesList = new ArrayList<>();
        speciesList.add(species);
        Employee employee = anEmployee("Andrei");
        employee.setSpecializations(speciesList);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);
        SpecializationBetweenHours specializationBetweenHours = new SpecializationBetweenHours("Bear", startTime, endTime);

        when(employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours)).thenReturn(employees);

        mockMvc.perform(get("/api/employees/specialization")
                .content(objectMapper.writeValueAsString(specializationBetweenHours))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Andrei")))
                .andExpect(jsonPath("$[0].specializations[0].name", is(species.getName())));
    }

    @Test
    void getAllEmployeesWithSpecializationSpeciesNotFound() throws Exception{
        SpecializationBetweenHours specializationBetweenHours = new SpecializationBetweenHours("Bear",null, null);

        when(employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours)).thenThrow(
                new ResourceNotFoundException("Method getAllEmployeesWithSpecialization: Species not found."));

        mockMvc.perform(get("/api/employees/specialization")
                .content(objectMapper.writeValueAsString(specializationBetweenHours))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAllEmployeesWithSpecialization: Species not found.")));
    }
}