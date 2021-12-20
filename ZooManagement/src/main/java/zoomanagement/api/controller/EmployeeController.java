package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.DTO.SpecializationBetweenHours;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.EmployeeService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping(value = "/specialization")
    public ResponseEntity<List<Employee>> getAllEmployeesWithSpecialization (@Valid @RequestBody SpecializationBetweenHours specializationBetweenHours) throws ResourceNotFoundException {
        List<Employee> employees = employeeService.getAllEmployeesWithSpecialization(
                specializationBetweenHours.getName(),
                specializationBetweenHours.getStartTime(),
                specializationBetweenHours.getEndTime());
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
