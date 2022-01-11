package zoomanagement.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.DTO.SpecializationBetweenHours;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.exception.ExceptionResponse;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.exception.ValidationExceptionResponse;
import zoomanagement.api.service.EmployeeService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/employees")
public class EmployeeController {
    private final EmployeeService employeeService;

    @ApiOperation(
            value = "Get all employees with a certain specialization",
            notes = "Retrieves all employees who have the provided name of species among their specializations. " +
                    "If an interval of time is provided, the list of employees is filtered to include only those who work in that interval.",
            response = Employee.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = Employee.class),
            @ApiResponse(code=404, message="Species not found", response = ExceptionResponse.class),
            @ApiResponse(code=400, message="Invalid data", response = ValidationExceptionResponse.class)
    })
    @GetMapping(value = "/specialization")
    public ResponseEntity<List<Employee>> getAllEmployeesWithSpecialization (@Valid @RequestBody SpecializationBetweenHours specializationBetweenHours) throws ResourceNotFoundException {
        List<Employee> employees = employeeService.getAllEmployeesWithSpecialization(specializationBetweenHours);
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }
}
