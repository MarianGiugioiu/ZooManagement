package zoomanagement.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Species;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AnimalRepository extends JpaRepository<Animal, UUID>{
    Optional<Animal> findByName(String name);
    @Query("select u from Animal u where (:age is null or u.age = :age) and (:sex is null or u.sex = :sex) and ((:species is null or u.species = :species))")
    List<Animal> findAllAnimalsByAgeAndSexAndSpecies(@Param("age") String age, @Param("sex") String sex, @Param("species") Species species);
}
