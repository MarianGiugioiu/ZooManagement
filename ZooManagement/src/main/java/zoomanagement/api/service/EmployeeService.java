package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.DaySchedule;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.EmployeeRepository;
import zoomanagement.api.repository.SpeciesRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService implements ServiceInterface<Employee>{
    private final EmployeeRepository employeeRepository;
    private final SpeciesRepository speciesRepository;
    private final Utils utils;

    @Override
    public List<Employee> getAll() {
        log.info("Fetching all employees...");
        return employeeRepository.findAll();
    }

    @Override
    public Employee getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching employee with id {}...", id);
        return employeeRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Employee not found.");
                    return new ResourceNotFoundException("Method getOneById: Employee not found.");
                }
        );
    }

    @Override
    public Employee add(Employee entry){
        log.info("Adding employee {}...", entry.getName());

        return employeeRepository.save(entry);
    }

    @Override
    public Employee update(UUID id, Employee entry) throws ResourceNotFoundException{
        if(employeeRepository.findById(id).isPresent()) {
            log.info("Updating employee with id {}...", id);
            entry.setId(id);
            return employeeRepository.save(entry);
        }
        else {
            log.error("Employee not found in the database.");
            throw new ResourceNotFoundException("Method update: Employee not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(employeeRepository.findById(id).isPresent()) {
            log.info("Deleting employee with id {}...", id);
            employeeRepository.deleteById(id);
        }
        else {
            log.error("Employee not found in the database.");
            throw new ResourceNotFoundException("Method delete: Employee not found.");
        }
    }

    public List<Employee> getAllEmployeesWithSpecialization(String speciesName, LocalDateTime startTime, LocalDateTime endTime) throws ResourceNotFoundException {
        Species species = speciesRepository.findByName(speciesName).orElseThrow(
            () -> {
                log.error("Species not found.");
                return new ResourceNotFoundException("Method getAllEmployeesWithSpecialization: Species not found.");
            }
        );
        List<Employee> employeesWithSpecialization = employeeRepository.findAllBySpecializationsContaining(species);
        if (startTime == null) {
            return employeesWithSpecialization;
        }

        List<Employee> filteredEmployeesWithSpecialization = new ArrayList<>();
        for (Employee employee : employeesWithSpecialization) {
            log.info(employee.getSchedule());
            List<DaySchedule> scheduleList = utils.getScheduleList(employee.getSchedule());
            /*for (DaySchedule daySchedule : scheduleList) {
                log.info(daySchedule.toString());
            }*/
            if (utils.scheduleContains(scheduleList, startTime, endTime)) {
                filteredEmployeesWithSpecialization.add(employee);
            }
        }

        return filteredEmployeesWithSpecialization;
    }
}
