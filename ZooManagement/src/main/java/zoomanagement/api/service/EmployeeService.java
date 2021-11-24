package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.EmployeeRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeService implements ServiceInterface<Employee>{
    private final EmployeeRepository employeeRepository;

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
}
