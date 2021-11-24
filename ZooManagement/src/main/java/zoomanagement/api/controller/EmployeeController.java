package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
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
@RequestMapping("/employees")
public class EmployeeController {
    private final EmployeeService employeeService;



    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Employee>> getAllEmployees () {
        List<Employee> employees = employeeService.getAll();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Employee employee;
        employee = employeeService.getOneById(id);
        return new ResponseEntity<>(employee, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Employee> addEmployee (@Valid @RequestBody Employee employee) {
        Employee savedEmployee;
        savedEmployee = employeeService.add(employee);
        return new ResponseEntity<>(savedEmployee, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable( "id" ) UUID id, @Valid @RequestBody Employee employee ) throws ResourceNotFoundException {
        Employee updatedEmployee;
        updatedEmployee = employeeService.update(id, employee);
        return new ResponseEntity<>(updatedEmployee, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteEmployee(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        employeeService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
