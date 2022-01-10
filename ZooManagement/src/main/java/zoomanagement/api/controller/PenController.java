package zoomanagement.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.Activity;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.exception.ExceptionResponse;
import zoomanagement.api.exception.PenAlreadyUsedException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.exception.ValidationExceptionResponse;
import zoomanagement.api.service.PenService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/pens")
public class PenController {
    private final PenService penService;

    @ApiOperation(
            value = "Get all pens with animal eating a certain food",
            notes = "Retrieves all Diets which contain the provided food and returns a list of Pens corresponding to the Animals who have that diet.",
            response = Set.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = Set.class)
    })
    @GetMapping(value = "/food/{food}")
    public ResponseEntity<Set<Pen>> getAllPensWithAnimalsEating (@PathVariable("food") String food) {
        Set<Pen> pens = penService.getAllPensWithAnimalsEating(food);
        return new ResponseEntity<>(pens, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Change Pen",
            notes = "Moves all animals from previous pen to new pen and returns the modified new pen.",
            response = Pen.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = Pen.class),
            @ApiResponse(code=404, message="Previous or new pen is not found", response = ExceptionResponse.class),
            @ApiResponse(code=409, message="New pen is already used", response = ExceptionResponse.class)
    })
    @PatchMapping(value = "/change")
    public ResponseEntity<Pen> changePen(@RequestParam String previousPen, @RequestParam String newPen) throws ResourceNotFoundException, PenAlreadyUsedException {
        Pen pen = penService.changePen(previousPen, newPen);
        return new ResponseEntity<>(pen, HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get a map of the Zoo",
            notes = "Creates a list of all active pens sorted by their location",
            response = List.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = List.class)
    })
    @GetMapping(value = "map")
    public ResponseEntity<List<PenDTO>> getMap () {
        List<PenDTO> pens = penService.getMap();
        return new ResponseEntity<>(pens, HttpStatus.OK);
    }
}
