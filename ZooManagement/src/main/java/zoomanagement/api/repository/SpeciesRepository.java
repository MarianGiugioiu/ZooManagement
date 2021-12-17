package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Species;

import java.util.Optional;
import java.util.UUID;

public interface SpeciesRepository extends JpaRepository<Species, UUID> {
    public Optional<Species> findByName(String name);
}
