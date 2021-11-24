package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.ActivityService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activitys")
public class ActivityController {
    private final ActivityService activityService;



    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Activity>> getAllActivitys () {
        List<Activity> activitys = activityService.getAll();
        return new ResponseEntity<>(activitys, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Activity activity;
        activity = activityService.getOneById(id);
        return new ResponseEntity<>(activity, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Activity> addActivity (@Valid @RequestBody Activity activity) {
        Activity savedActivity;
        savedActivity = activityService.add(activity);
        return new ResponseEntity<>(savedActivity, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable( "id" ) UUID id, @Valid @RequestBody Activity activity ) throws ResourceNotFoundException {
        Activity updatedActivity;
        updatedActivity = activityService.update(id, activity);
        return new ResponseEntity<>(updatedActivity, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteActivity(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        activityService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
