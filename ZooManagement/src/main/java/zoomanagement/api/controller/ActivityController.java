package zoomanagement.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.DTO.ActivityDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.exception.EmployeeBusyException;
import zoomanagement.api.exception.ExceptionResponse;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.exception.ValidationExceptionResponse;
import zoomanagement.api.service.ActivityService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/activities")
public class ActivityController {
    private final ActivityService activityService;

    @PostMapping(value = "")
    @ApiOperation(
            value = "Add new Activity",
            notes = "Returns an Activity saved in the DB, based on the information provided by the ActivityDTO",
            response = Activity.class
    )
    @ApiResponses(value={
            @ApiResponse(code=201, message="Activity created", response = Activity.class),
            @ApiResponse(code=404, message="An employee is not found", response = ExceptionResponse.class),
            @ApiResponse(code=400, message="Invalid data", response = ValidationExceptionResponse.class),
            @ApiResponse(code=409, message="An employee is busy with another activity", response = ExceptionResponse.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Activity> addActivity (@Valid @RequestBody ActivityDTO activity) throws ResourceNotFoundException, EmployeeBusyException {
        Activity savedActivity = activityService.add(activity);
        return new ResponseEntity<>(savedActivity, HttpStatus.CREATED);
    }
}
