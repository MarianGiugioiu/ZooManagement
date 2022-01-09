package zoomanagement.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.domain.PenStatusType;
import zoomanagement.api.exception.EmployeeBusyException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.ActivityRepository;
import zoomanagement.api.repository.EmployeeRepository;
import zoomanagement.api.repository.PenRepository;

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

    @Mock
    private PenRepository penRepository;

    @Mock
    private ActivityRepository activityRepository;

    @Test
    void add() throws ResourceNotFoundException, EmployeeBusyException {
        //Arrange
        ActivityDTO activityDTO = anActivityDTO("Cleaning BearPublic",
                new ArrayList<>(Arrays.asList("Alin")),
                "cleaning",
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30),
                "BearPublic"
                );
        Employee employee = anEmployee("Alin");
        Activity previousActivity = anActivity(LocalDateTime.of(2022, 1, 8, 16, 30),
                LocalDateTime.of(2022, 1, 8, 17, 30));
        employee.setActivities(new ArrayList<>(Arrays.asList(previousActivity)));
        Pen pen = aPen("BearPublic");

        pen.setStatus(PenStatusType.maintenance.name());

        Activity activity = Activity.builder()
                .name(activityDTO.getName())
                .action(activityDTO.getAction())
                .status("not started")
                .startTime(activityDTO.getStartTime())
                .endTime(activityDTO.getEndTime())
                .pen(pen)
                .employees(new ArrayList<>(Arrays.asList(employee)))
                .build();

        when(employeeRepository.findByName("Alin")).thenReturn(Optional.of(employee));
        when(penRepository.findByName("BearPublic")).thenReturn(Optional.of(pen));
        when(penRepository.save(pen)).thenReturn(pen);
        when(activityRepository.save(activity)).thenReturn(activity);

        //Act
        Activity result = activityService.add(activityDTO);

        //Assert
        assertEquals(activity, result);
        verify(employeeRepository, times(1)).findByName("Alin");
        verify(penRepository, times(1)).findByName("BearPublic");
        verify(activityRepository, times(1)).save(activity);
        verify(penRepository, times(1)).save(pen);
        verifyNoMoreInteractions(penRepository);
        //verifyNoInteractions(employeeRepository);
        //verifyNoInteractions(activityRepository);
    }

    @Test
    void addPenNotFound() {
        ActivityDTO activityDTO = anActivityDTO(new ArrayList<>(Arrays.asList("Alin")),
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30));
        Employee employee = anEmployee("Alin");
        Activity activity = anActivity(LocalDateTime.of(2022, 1, 8, 16, 30),
                LocalDateTime.of(2022, 1, 8, 17, 30));
        employee.setActivities(new ArrayList<>(Arrays.asList(activity)));

        when(employeeRepository.findByName("Alin")).thenReturn(Optional.of(employee));
        when(penRepository.findByName(anyString())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> activityService.add(activityDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method add: Pen not found.");
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
                LocalDateTime.of(2022, 1, 8, 13, 30),
                LocalDateTime.of(2022, 1, 8, 15, 30));
        Employee employee = anEmployee("Alin");
        Activity activity = anActivity(LocalDateTime.of(2022, 1, 8, 14, 30),
                LocalDateTime.of(2022, 1, 8, 16, 30));
        employee.setActivities(new ArrayList<>(Arrays.asList(activity)));

        when(employeeRepository.findByName("Alin")).thenReturn(Optional.of(employee));

        //Act
        assertThatThrownBy(() -> activityService.add(activityDTO))
                .isInstanceOf(EmployeeBusyException.class)
                .hasMessageContaining("Method add: Employee is busy with another activity.");
    }
}