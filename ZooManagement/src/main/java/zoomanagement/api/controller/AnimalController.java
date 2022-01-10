package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.DTO.BabyAnimal;
import zoomanagement.api.DTO.GenealogicalTree;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.exception.AnimalMissingInGenealogicalTreeException;
import zoomanagement.api.exception.ResourceNotFoundException;
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

    @GetMapping(value = "/genealogical_tree")
    public ResponseEntity<Animal> getAnimalFromGenealogicalTree(@Valid @RequestBody GenealogicalTree genealogicalTree) throws AnimalMissingInGenealogicalTreeException, ResourceNotFoundException {
        return new ResponseEntity<>(animalService.getAnimalFromGenealogicalTree(genealogicalTree), HttpStatus.OK);
    }

    @GetMapping(value = "optional")
    public ResponseEntity<List<Animal>> getAllAnimalsByAgeAndSexAndSpecies(@RequestParam(required = false) String age, @RequestParam(required = false) String sex, @RequestParam(required = false) String speciesName) throws ResourceNotFoundException {
        return new ResponseEntity<>(animalService.getAllAnimalsByAgeAndSexAndSpecies(age, sex, speciesName), HttpStatus.OK);
    }

    @GetMapping(value = "unique")
    public ResponseEntity<Map<String, Animal>> getUniqueAnimals(){
        return new ResponseEntity<>(animalService.getUniqueAnimals(), HttpStatus.OK);
    }

    @PostMapping(value = "baby")
    public ResponseEntity<Animal> addBabyAnimal(@Valid @RequestBody BabyAnimal babyAnimal) throws ResourceNotFoundException {
        return new ResponseEntity<>(animalService.addBabyAnimal(babyAnimal), HttpStatus.OK);
    }

    @GetMapping(value = "/conditions/{name}")
    public ResponseEntity<String> getAnimalConditions(@PathVariable("name") String name) throws ResourceNotFoundException {
        return new ResponseEntity<>(animalService.getAnimalConditions(name), HttpStatus.OK);
    }

}
