package zoomanagement.api.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.DaySchedule;
import zoomanagement.api.DTO.SpecializationBetweenHours;
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
@AllArgsConstructor
public class EmployeeService{
    private final EmployeeRepository employeeRepository;
    private final SpeciesRepository speciesRepository;

    public List<Employee> getAllEmployeesWithSpecialization(SpecializationBetweenHours specializationBetweenHours) throws ResourceNotFoundException {
        //Gets data from the DTO
        String speciesName = specializationBetweenHours.getName();
        LocalDateTime startTime = specializationBetweenHours.getStartTime();
        LocalDateTime endTime = specializationBetweenHours.getEndTime();

        //Searches for species
        Species species = speciesRepository.findByName(speciesName).orElseThrow(
            () -> {
                log.error("Species not found.");
                return new ResourceNotFoundException("Method getAllEmployeesWithSpecialization: Species not found.");
            }
        );
        //Searches for all employees with the given species among their specializations
        List<Employee> employeesWithSpecialization = employeeRepository.findAllBySpecializationsContaining(species);


        if (startTime == null) {
            return employeesWithSpecialization;
        }

        //If time interval is provided, gets the list of DaySchedule of each employee and selects those who work in the given interval
        List<Employee> filteredEmployeesWithSpecialization = new ArrayList<>();
        for (Employee employee : employeesWithSpecialization) {
            List<DaySchedule> scheduleList = Utils.getScheduleList(employee.getSchedule());
            if (Utils.scheduleContains(scheduleList, startTime, endTime)) {
                filteredEmployeesWithSpecialization.add(employee);
            }
        }

        return filteredEmployeesWithSpecialization;
    }
}
