package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.SpeciesService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/species")
public class SpeciesController {
    private final SpeciesService speciesService;



    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Species>> getAllSpeciess () {
        List<Species> species = speciesService.getAll();
        return new ResponseEntity<>(species, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Species> getSpeciesById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Species species;
        species = speciesService.getOneById(id);
        return new ResponseEntity<>(species, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Species> addSpecies (@Valid @RequestBody Species species) {
        Species savedSpecies;
        savedSpecies = speciesService.add(species);
        return new ResponseEntity<>(savedSpecies, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Species> updateSpecies(@PathVariable( "id" ) UUID id, @Valid @RequestBody Species species ) throws ResourceNotFoundException {
        Species updatedSpecies;
        updatedSpecies = speciesService.update(id, species);
        return new ResponseEntity<>(updatedSpecies, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deleteSpecies(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        speciesService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
