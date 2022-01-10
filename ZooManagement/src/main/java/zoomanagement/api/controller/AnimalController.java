package zoomanagement.api.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.DTO.BabyAnimal;
import zoomanagement.api.DTO.GenealogicalTree;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Employee;
import zoomanagement.api.exception.AnimalMissingInGenealogicalTreeException;
import zoomanagement.api.exception.ExceptionResponse;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.exception.ValidationExceptionResponse;
import zoomanagement.api.service.AnimalService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/animals")
public class AnimalController {
    private final AnimalService animalService;

    @ApiOperation(
            value = "Get an animal from a genealogical tree",
            notes = "Retrieves an animal from a genealogical tree starting from the animal with the provided name, and continuing based on the list of parents." +
                    "If the list contains, for example, father and mother, the mother of the father of the first animal is retrieved.",
            response = Animal.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = Animal.class),
            @ApiResponse(code=404, message="An animal is not found", response = ExceptionResponse.class),
            @ApiResponse(code=400, message="Invalid data", response = ValidationExceptionResponse.class)
    })
    @GetMapping(value = "/genealogical_tree")
    public ResponseEntity<Animal> getAnimalFromGenealogicalTree(@Valid @RequestBody GenealogicalTree genealogicalTree) throws AnimalMissingInGenealogicalTreeException, ResourceNotFoundException {
        return new ResponseEntity<>(animalService.getAnimalFromGenealogicalTree(genealogicalTree), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Gets all animals filtered by age, sex and species",
            notes = "Retrieves a list of Animals filtered by the optional parameters age, sex and name of species.",
            response = List.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = List.class),
            @ApiResponse(code=404, message="Species not found", response = ExceptionResponse.class)
    })
    @GetMapping(value = "optional")
    public ResponseEntity<List<Animal>> getAllAnimalsByAgeAndSexAndSpecies(@RequestParam(required = false) String age, @RequestParam(required = false) String sex, @RequestParam(required = false) String speciesName) throws ResourceNotFoundException {
        return new ResponseEntity<>(animalService.getAllAnimalsByAgeAndSexAndSpecies(age, sex, speciesName), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get the most peculiar animal of each species",
            notes = "Returns a map with names of species for keys and animals for values." +
                    "Each animal has the most peculiarities or the most complex ones in its species.",
            response = Map.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = Map.class)
    })
    @GetMapping(value = "unique")
    public ResponseEntity<Map<String, Animal>> getUniqueAnimals(){
        return new ResponseEntity<>(animalService.getUniqueAnimals(), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Add a new Animal",
            notes = "Creates and returns a baby animal based on the information provided by the BabyAnimal." +
                    "Also creates the associated diet.",
            response = Animal.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = Animal.class),
            @ApiResponse(code=404, message="An animal is not found", response = ExceptionResponse.class),
            @ApiResponse(code=400, message="Invalid data", response = ValidationExceptionResponse.class)
    })
    @PostMapping(value = "baby")
    public ResponseEntity<Animal> addBabyAnimal(@Valid @RequestBody BabyAnimal babyAnimal) throws ResourceNotFoundException {
        return new ResponseEntity<>(animalService.addBabyAnimal(babyAnimal), HttpStatus.OK);
    }

    @ApiOperation(
            value = "Get conditions of animal",
            notes = "Returns a message based on the ratios between the preferences and recommendations in the diet" +
                    "of the animal with the provided name and between its natural habitat and the habitat of its pen.",
            response = String.class
    )
    @ApiResponses(value={
            @ApiResponse(code=200, message="Ok", response = String.class),
            @ApiResponse(code=404, message="Animal not found", response = ExceptionResponse.class)
    })
    @GetMapping(value = "/conditions/{name}")
    public ResponseEntity<String> getAnimalConditions(@PathVariable("name") String name) throws ResourceNotFoundException {
        return new ResponseEntity<>(animalService.getAnimalConditions(name), HttpStatus.OK);
    }

}
