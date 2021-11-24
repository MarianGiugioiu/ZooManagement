package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Animal;

import java.util.UUID;

public interface AnimalRepository extends JpaRepository<Animal, UUID>{
}
