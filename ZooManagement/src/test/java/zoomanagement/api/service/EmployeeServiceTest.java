package zoomanagement.api.service;

import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import zoomanagement.api.DTO.SpecializationBetweenHours;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.EmployeeRepository;
import zoomanagement.api.repository.SpeciesRepository;

import static zoomanagement.api.util.MockDataUtils.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {
    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private SpeciesRepository speciesRepository;

    @Test
    @DisplayName("Test getAllEmployeesWithSpecialization returns correct employees when no interval is provided.")
    void getAllEmployeesWithSpecializationWithoutIntervals() throws ResourceNotFoundException {
        //Arrange
        Species species = aSpecies("Bear");
        List<Species> speciesList = new ArrayList<>();
        speciesList.add(species);
        Employee employee = anEmployee("Andrei");
        employee.setSpecializations(speciesList);
        List<Employee> employees = new ArrayList<>();
        employees.add(employee);

        when(speciesRepository.findByName("Bear")).thenReturn(Optional.of(species));
        when(employeeRepository.findAllBySpecializationsContaining(species))
                .thenReturn(employees);

        SpecializationBetweenHours specializationBetweenHours = new SpecializationBetweenHours("Bear",null, null);

        //Act
        List<Employee> result = employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours);

        //Assert
        assertEquals(employees, result);
    }

    @Test
    @DisplayName("Test getAllEmployeesWithSpecialization returns correct employees when interval is provided.")
    void getAllEmployeesWithSpecializationWithIntervals() throws ResourceNotFoundException {
        //Arrange
        LocalDateTime startTime = LocalDateTime.of(2022, 01, 8, 13, 30);
        LocalDateTime endTime = LocalDateTime.of(2022, 01, 8, 15, 30);

        String schedule1 = "monday-10:00-18:00";
        String schedule2 = "monday:sunday-10:00-18:00";

        Species species = aSpecies("Bear");
        List<Species> speciesList = new ArrayList<>();
        speciesList.add(species);

        Employee employee1 = anEmployee("Andrei");
        employee1.setSpecializations(speciesList);
        employee1.setSchedule(schedule1);
        Employee employee2 = anEmployee("Alin");
        employee2.setSpecializations(speciesList);
        employee2.setSchedule(schedule2);

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        List<Employee> filteredEmployees = new ArrayList<>();
        filteredEmployees.add(employee2);

        when(speciesRepository.findByName("Bear")).thenReturn(Optional.of(species));
        when(employeeRepository.findAllBySpecializationsContaining(species))
                .thenReturn(employees);

        SpecializationBetweenHours specializationBetweenHours = new SpecializationBetweenHours("Bear",startTime,endTime);

        //Act
        List<Employee> result = employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours);

        //Assert
        assertEquals(filteredEmployees, result);
    }

    @Test
    @DisplayName("Test getAllEmployeesWithSpecialization returns ResourceNotFoundException for species.")
    void getAllEmployeesWithSpecializationSpeciesNotFound() {
        when(speciesRepository.findByName("Bear")).thenReturn(Optional.empty());
        SpecializationBetweenHours specializationBetweenHours = new SpecializationBetweenHours("Bear",null, null);
        //Act
        assertThatThrownBy(() -> employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method getAllEmployeesWithSpecialization: Species not found.");
    }
}