package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.AnimalService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/animals")
public class AnimalController {
    private final AnimalService animalService;



    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Animal>> getAllAnimals () {
        List<Animal> animals = animalService.getAll();
        return new ResponseEntity<>(animals, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Animal animal;
            animal = animalService.getOneById(id);
        return new ResponseEntity<>(animal, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Animal> addAnimal (@Valid @RequestBody Animal animal) {
        Animal savedAnimal;
        savedAnimal = animalService.add(animal);
        return new ResponseEntity<>(savedAnimal, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Animal> updateAnimal(@PathVariable( "id" ) UUID id, @Valid @RequestBody Animal animal ) throws ResourceNotFoundException {
        Animal updatedAnimal;
            updatedAnimal = animalService.update(id, animal);
        return new ResponseEntity<>(updatedAnimal, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteAnimal(@PathVariable("id") UUID id) throws ResourceNotFoundException {
            animalService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}