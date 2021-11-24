package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zoomanagement.api.domain.Activity;

import java.util.UUID;

public interface ActivityRepository extends JpaRepository<Activity, UUID> {
}
