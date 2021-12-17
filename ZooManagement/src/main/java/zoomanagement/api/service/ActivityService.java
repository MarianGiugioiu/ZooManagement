package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Pen;
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
public class ActivityService implements ServiceInterface<Activity>{
    private final ActivityRepository activityRepository;
    private final EmployeeRepository employeeRepository;
    private final PenRepository penRepository;

    @Override
    public List<Activity> getAll() {
        log.info("Fetching all activitys...");
        return activityRepository.findAll();
    }

    @Override
    public Activity getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching activity with id {}...", id);
        return activityRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Activity not found.");
                    return new ResourceNotFoundException("Method getOneById: Activity not found.");
                }
        );
    }

    @Override
    public Activity add(Activity entry){
        log.info("Adding activity {}...", entry.getName());

        return activityRepository.save(entry);
    }

    public Activity add(ActivityDTO activityDTO) throws ResourceNotFoundException, EmployeeBusyException {
        List<Employee> employees = new ArrayList<>();
        for (String employeeName : activityDTO.getEmployeeNames()) {
            Employee employee = employeeRepository.findByName(employeeName).orElseThrow(
                () -> {
                    log.error("Employee not found.");
                    return new ResourceNotFoundException("Method add: Employee not found.");
                }
            );
            for (Activity activityPerEmployee : employee.getActivities()) {
                if (activityPerEmployee.getStartTime().isBefore(activityDTO.getEndTime())
                        && activityPerEmployee.getEndTime().isAfter(activityDTO.getStartTime())) {
                    throw new EmployeeBusyException("Method add: Employee is busy with another activity.");
                }
            }

            employees.add(employee);
        }
        Pen pen = penRepository.findByName(activityDTO.getPenName()).orElseThrow(
            () -> {
                log.error("Pen not found.");
                return new ResourceNotFoundException("Method add: Pen not found.");
            }
        );

        String action = activityDTO.getAction();

        if (action.contains("feeding") || action.contains("cleaning")) {
            pen.setStatus("maintenance");
        } else if (action.contains("repair") || action.contains("build")) {
            pen.setStatus("inactive");
        }

        pen = penRepository.save(pen);

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

    @Override
    public Activity update(UUID id, Activity entry) throws ResourceNotFoundException{
        if(activityRepository.findById(id).isPresent()) {
            log.info("Updating activity with id {}...", id);
            entry.setId(id);
            return activityRepository.save(entry);
        }
        else {
            log.error("Activity not found in the database.");
            throw new ResourceNotFoundException("Method update: Activity not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(activityRepository.findById(id).isPresent()) {
            log.info("Deleting activity with id {}...", id);
            activityRepository.deleteById(id);
        }
        else {
            log.error("Activity not found in the database.");
            throw new ResourceNotFoundException("Method delete: Activity not found.");
        }
    }
}
