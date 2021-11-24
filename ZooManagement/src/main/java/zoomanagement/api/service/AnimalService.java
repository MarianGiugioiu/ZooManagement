package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.AnimalRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimalService implements ServiceInterface<Animal>{
    private final AnimalRepository animalRepository;

    @Override
    public List<Animal> getAll() {
        log.info("Fetching all animals...");
        return animalRepository.findAll();
    }

    @Override
    public Animal getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching animal with id {}...", id);
        return animalRepository.findById(id).orElseThrow(
            () -> {
                log.error("Animal not found.");
                return new ResourceNotFoundException("Method getOneById: Animal not found.");
            }
        );
    }

    @Override
    public Animal add(Animal entry){
        log.info("Adding animal {}...", entry.getName());

        return animalRepository.save(entry);
    }

    @Override
    public Animal update(UUID id, Animal entry) throws ResourceNotFoundException{
        if(animalRepository.findById(id).isPresent()) {
            log.info("Updating animal with id {}...", id);
            entry.setId(id);
            return animalRepository.save(entry);
        }
        else {
            log.error("Animal not found in the database.");
            throw new ResourceNotFoundException("Method update: Animal not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(animalRepository.findById(id).isPresent()) {
            log.info("Deleting animal with id {}...", id);
            animalRepository.deleteById(id);
        }
        else {
            log.error("Animal not found in the database.");
            throw new ResourceNotFoundException("Method delete: Animal not found.");
        }
    }
}
