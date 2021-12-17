package zoomanagement.api.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.exception.PenAlreadyUsedException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.PenService;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pens")
public class PenController {
    private final PenService penService;

    @GetMapping(value = {"", "/"})
    public ResponseEntity<List<Pen>> getAllPens () {
        List<Pen> pens = penService.getAll();
        return new ResponseEntity<>(pens, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Pen> getPenById(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        Pen pen;
        pen = penService.getOneById(id);
        return new ResponseEntity<>(pen, HttpStatus.OK);
    }

    @PostMapping(value = {"", "/"})
    public ResponseEntity<Pen> addPen (@Valid @RequestBody Pen pen) {
        Pen savedPen;
        savedPen = penService.add(pen);
        return new ResponseEntity<>(savedPen, HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<Pen> updatePen(@PathVariable( "id" ) UUID id, @Valid @RequestBody Pen pen ) throws ResourceNotFoundException {
        Pen updatedPen;
        updatedPen = penService.update(id, pen);
        return new ResponseEntity<>(updatedPen, HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<HttpStatus> deletePen(@PathVariable("id") UUID id) throws ResourceNotFoundException {
        penService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/food/{food}")
    public ResponseEntity<Set<Pen>> getAllPensWithAnimalsEating (@PathVariable("food") String food) {
        Set<Pen> pens = penService.getAllPensWithAnimalsEating(food);
        return new ResponseEntity<>(pens, HttpStatus.OK);
    }

    @PostMapping(value = "/change")
    public ResponseEntity<HttpStatus> changePen(@RequestParam String previousPen, @RequestParam String newPen) throws ResourceNotFoundException, PenAlreadyUsedException {
        penService.changePen(previousPen, newPen);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "map")
    public ResponseEntity<List<PenDTO>> getMap () {
        List<PenDTO> pens = penService.getMap();
        return new ResponseEntity<>(pens, HttpStatus.OK);
    }
}
