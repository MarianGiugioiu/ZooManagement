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
@RequestMapping("/api/pens")
public class PenController {
    private final PenService penService;

    @GetMapping(value = "/food/{food}")
    public ResponseEntity<Set<Pen>> getAllPensWithAnimalsEating (@PathVariable("food") String food) {
        Set<Pen> pens = penService.getAllPensWithAnimalsEating(food);
        return new ResponseEntity<>(pens, HttpStatus.OK);
    }

    @PatchMapping(value = "/change")
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
