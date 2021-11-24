package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.ActivityRepository;

import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ActivityService implements ServiceInterface<Activity>{
    private final ActivityRepository activityRepository;

    @Override
    public List<Activity> getAll() {
        log.info("Fetching all activitys...");
        return activityRepository.findAll();
    }

    @Override
    public Activity getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching activity with id {}...", id);
        return activityRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Activity not found.");
                    return new ResourceNotFoundException("Method getOneById: Activity not found.");
                }
        );
    }

    @Override
    public Activity add(Activity entry){
        log.info("Adding activity {}...", entry.getName());

        return activityRepository.save(entry);
    }

    @Override
    public Activity update(UUID id, Activity entry) throws ResourceNotFoundException{
        if(activityRepository.findById(id).isPresent()) {
            log.info("Updating activity with id {}...", id);
            entry.setId(id);
            return activityRepository.save(entry);
        }
        else {
            log.error("Activity not found in the database.");
            throw new ResourceNotFoundException("Method update: Activity not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(activityRepository.findById(id).isPresent()) {
            log.info("Deleting activity with id {}...", id);
            activityRepository.deleteById(id);
        }
        else {
            log.error("Activity not found in the database.");
            throw new ResourceNotFoundException("Method delete: Activity not found.");
        }
    }
}
