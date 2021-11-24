package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Employee;

import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
}
