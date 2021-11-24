package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.DietService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diets")
public class DietController {
    private final DietService dietService;



    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Diet>> getAllDiets () {
        List<Diet> diets = dietService.getAll();
        return new ResponseEntity<>(diets, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Diet> getDietById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Diet diet;
        diet = dietService.getOneById(id);
        return new ResponseEntity<>(diet, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Diet> addDiet (@Valid @RequestBody Diet diet) {
        Diet savedDiet;
        savedDiet = dietService.add(diet);
        return new ResponseEntity<>(savedDiet, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Diet> updateDiet(@PathVariable( "id" ) UUID id, @Valid @RequestBody Diet diet ) throws ResourceNotFoundException {
        Diet updatedDiet;
        updatedDiet = dietService.update(id, diet);
        return new ResponseEntity<>(updatedDiet, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteDiet(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        dietService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
