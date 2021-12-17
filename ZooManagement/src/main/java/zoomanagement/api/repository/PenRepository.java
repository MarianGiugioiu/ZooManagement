package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import zoomanagement.api.domain.Pen;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PenRepository extends JpaRepository<Pen, UUID> {
    Optional<Pen> findByName(String name);
}
