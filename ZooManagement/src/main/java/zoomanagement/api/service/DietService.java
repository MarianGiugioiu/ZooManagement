package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.DietRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class DietService implements ServiceInterface<Diet>{
    private final DietRepository dietRepository;

    @Override
    public List<Diet> getAll() {
        log.info("Fetching all diets...");
        return dietRepository.findAll();
    }

    @Override
    public Diet getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching diet with id {}...", id);
        return dietRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Diet not found.");
                    return new ResourceNotFoundException("Method getOneById: Diet not found.");
                }
        );
    }

    @Override
    public Diet add(Diet entry){
        //log.info("Adding diet {}...", entry.getName());

        return dietRepository.save(entry);
    }

    @Override
    public Diet update(UUID id, Diet entry) throws ResourceNotFoundException{
        if(dietRepository.findById(id).isPresent()) {
            log.info("Updating diet with id {}...", id);
            entry.setId(id);
            return dietRepository.save(entry);
        }
        else {
            log.error("Diet not found in the database.");
            throw new ResourceNotFoundException("Method update: Diet not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(dietRepository.findById(id).isPresent()) {
            log.info("Deleting diet with id {}...", id);
            dietRepository.deleteById(id);
        }
        else {
            log.error("Diet not found in the database.");
            throw new ResourceNotFoundException("Method delete: Diet not found.");
        }
    }
}
