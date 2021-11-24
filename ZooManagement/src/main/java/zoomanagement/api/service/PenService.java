package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.PenRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PenService implements ServiceInterface<Pen>{
    private final PenRepository penRepository;

    @Override
    public List<Pen> getAll() {
        log.info("Fetching all pens...");
        return penRepository.findAll();
    }

    @Override
    public Pen getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching pen with id {}...", id);
        return penRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Pen not found.");
                    return new ResourceNotFoundException("Method getOneById: Pen not found.");
                }
        );
    }

    @Override
    public Pen add(Pen entry){
        //log.info("Adding pen {}...", entry.getName());

        return penRepository.save(entry);
    }

    @Override
    public Pen update(UUID id, Pen entry) throws ResourceNotFoundException{
        if(penRepository.findById(id).isPresent()) {
            log.info("Updating pen with id {}...", id);
            entry.setId(id);
            return penRepository.save(entry);
        }
        else {
            log.error("Pen not found in the database.");
            throw new ResourceNotFoundException("Method update: Pen not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(penRepository.findById(id).isPresent()) {
            log.info("Deleting pen with id {}...", id);
            penRepository.deleteById(id);
        }
        else {
            log.error("Pen not found in the database.");
            throw new ResourceNotFoundException("Method delete: Pen not found.");
        }
    }
}
