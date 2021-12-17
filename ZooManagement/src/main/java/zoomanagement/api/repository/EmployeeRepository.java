package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Species;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID> {
    List<Employee> findAllBySpecializationsContaining(Species species);
    Optional<Employee> findByName(String name);
}
