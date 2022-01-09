package zoomanagement.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.exception.EmployeeBusyException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.EmployeeRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static zoomanagement.api.util.MockDataUtils.*;


import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ActivityServiceTest {

    @InjectMocks
    private ActivityService activityService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Test
    void add() {
    }

    @Test
    void addEmployeeNotFound() {
        //Arrange
        ActivityDTO activityDTO = anActivityDTO(new ArrayList<>(Arrays.asList("Alin")));
        when(employeeRepository.findByName("Alin")).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> activityService.add(activityDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method add: Employee not found.");
    }

    @Test
    void addEmployeeBusy() {
        //Arrange
        ActivityDTO activityDTO = anActivityDTO(new ArrayList<>(Arrays.asList("Alin")),
                LocalDateTime.of(2022, 01, 8, 13, 30),
                LocalDateTime.of(2022, 01, 8, 15, 30));
        Employee employee = anEmployee("Alin");
        Activity activity = anActivity(LocalDateTime.of(2022, 01, 8, 14, 30),
                LocalDateTime.of(2022, 01, 8, 16, 30));
        employee.setActivities(new ArrayList<>(Arrays.asList(activity)));

        when(employeeRepository.findByName("Alin")).thenReturn(Optional.of(employee));

        //Act
        assertThatThrownBy(() -> activityService.add(activityDTO))
                .isInstanceOf(EmployeeBusyException.class)
                .hasMessageContaining("Method add: Employee is busy with another activity.");
    }
}