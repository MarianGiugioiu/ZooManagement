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
public class EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final SpeciesRepository speciesRepository;
    private final Utils utils;

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
