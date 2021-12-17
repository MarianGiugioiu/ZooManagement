package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoomanagement.api.domain.Diet;

import java.util.List;
import java.util.UUID;

public interface DietRepository extends JpaRepository<Diet, UUID> {
    @Query("select u from Diet u where u.preferences like %:food%")
    List<Diet> findAllByPreferencesContaining(@Param("food") String food);
}
