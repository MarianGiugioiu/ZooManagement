package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Species;

import java.util.UUID;

public interface SpeciesRepository extends JpaRepository<Species, UUID> {
}
