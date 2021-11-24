package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.SpeciesRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpeciesService implements ServiceInterface<Species>{
    private final SpeciesRepository speciesRepository;

    @Override
    public List<Species> getAll() {
        log.info("Fetching all species...");
        return speciesRepository.findAll();
    }

    @Override
    public Species getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching species with id {}...", id);
        return speciesRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Species not found.");
                    return new ResourceNotFoundException("Method getOneById: Species not found.");
                }
        );
    }

    @Override
    public Species add(Species entry){
        log.info("Adding species {}...", entry.getName());

        return speciesRepository.save(entry);
    }

    @Override
    public Species update(UUID id, Species entry) throws ResourceNotFoundException{
        if(speciesRepository.findById(id).isPresent()) {
            log.info("Updating species with id {}...", id);
            entry.setId(id);
            return speciesRepository.save(entry);
        }
        else {
            log.error("Species not found in the database.");
            throw new ResourceNotFoundException("Method update: Species not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(speciesRepository.findById(id).isPresent()) {
            log.info("Deleting species with id {}...", id);
            speciesRepository.deleteById(id);
        }
        else {
            log.error("Species not found in the database.");
            throw new ResourceNotFoundException("Method delete: Species not found.");
        }
    }
}
