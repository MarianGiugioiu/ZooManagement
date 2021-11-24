package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Diet;

import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {
}
