package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService{
    private final ActivityRepository activityRepository;
    private final EmployeeRepository employeeRepository;
    private final PenRepository penRepository;

    public Activity add(ActivityDTO activityDTO) throws ResourceNotFoundException, EmployeeBusyException {
        List<Employee> employees = new ArrayList<>();
        //Searches for all needed employees
        for (String employeeName : activityDTO.getEmployeeNames()) {
            Employee employee = employeeRepository.findByName(employeeName).orElseThrow(
                () -> {
                    log.error("Employee not found.");
                    return new ResourceNotFoundException("Method add: Employee not found.");
                }
            );
            //Checks if the current employee is free for this activity
            for (Activity activityPerEmployee : employee.getActivities()) {
                if (activityPerEmployee.getStartTime().isBefore(activityDTO.getEndTime())
                        && activityPerEmployee.getEndTime().isAfter(activityDTO.getStartTime())) {
                    throw new EmployeeBusyException("Method add: Employee is busy with another activity.");
                }
            }

            employees.add(employee);
        }

        //Searches for pen
        Pen pen = penRepository.findByName(activityDTO.getPenName()).orElseThrow(
            () -> {
                log.error("Pen not found.");
                return new ResourceNotFoundException("Method add: Pen not found.");
            }
        );

        String action = activityDTO.getAction();

        //Sets the status of the pen based on the activity
        if (action.contains("feeding") || action.contains("cleaning")) {
            pen.setStatus(PenStatusType.maintenance.name());
        } else if (action.contains("repair") || action.contains("build")) {
            pen.setStatus(PenStatusType.inactive.name());
        }

        pen = penRepository.save(pen);

        //Creates the activity
        Activity activity = activityRepository.save(Activity.builder()
                .name(activityDTO.getName())
                .action(action)
                .status("not started")
                .startTime(activityDTO.getStartTime())
                .endTime(activityDTO.getEndTime())
                .pen(pen)
                .employees(employees)
                .build());

        return activity;
    }
}
