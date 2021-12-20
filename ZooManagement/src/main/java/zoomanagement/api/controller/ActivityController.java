package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.exception.EmployeeBusyException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.ActivityService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/activities")
public class ActivityController {
    private final ActivityService activityService;


    @PostMapping(value = {"", "/"})
    public ResponseEntity<Activity> addActivity (@Valid @RequestBody ActivityDTO activity) throws ResourceNotFoundException, EmployeeBusyException {
        Activity savedActivity = activityService.add(activity);
        return new ResponseEntity<>(savedActivity, HttpStatus.CREATED);
    }
}
