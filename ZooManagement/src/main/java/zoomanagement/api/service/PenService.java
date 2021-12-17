package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.exception.PenAlreadyUsedException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.DietRepository;
import zoomanagement.api.repository.PenRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PenService implements ServiceInterface<Pen>{
    private final PenRepository penRepository;
    private final DietRepository dietRepository;

    @Override
    public List<Pen> getAll() {
        log.info("Fetching all pens...");
        return penRepository.findAll();
    }

    @Override
    public Pen getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching pen with id {}...", id);
        return penRepository.findById(id).orElseThrow(
                () -> {
                    log.error("Pen not found.");
                    return new ResourceNotFoundException("Method getOneById: Pen not found.");
                }
        );
    }

    @Override
    public Pen add(Pen entry){
        //log.info("Adding pen {}...", entry.getName());

        return penRepository.save(entry);
    }

    @Override
    public Pen update(UUID id, Pen entry) throws ResourceNotFoundException{
        if(penRepository.findById(id).isPresent()) {
            log.info("Updating pen with id {}...", id);
            entry.setId(id);
            return penRepository.save(entry);
        }
        else {
            log.error("Pen not found in the database.");
            throw new ResourceNotFoundException("Method update: Pen not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(penRepository.findById(id).isPresent()) {
            log.info("Deleting pen with id {}...", id);
            penRepository.deleteById(id);
        }
        else {
            log.error("Pen not found in the database.");
            throw new ResourceNotFoundException("Method delete: Pen not found.");
        }
    }

    public Set<Pen> getAllPensWithAnimalsEating(String food) {
        Set<Pen> pens = new HashSet<>();
        List<Diet> diets = dietRepository.findAllByPreferencesContaining(" " + food + " ");
        log.info(String.valueOf(diets.size()));
        for (Diet diet : diets) {
            log.info(diet.getAnimal().getName());
            pens.add(diet.getAnimal().getPen());
        }
        return pens;
    }

    public void changePen(String previousPenName, String newPenName) throws ResourceNotFoundException, PenAlreadyUsedException {
        Pen previousPen = penRepository.findByName(previousPenName).orElseThrow(
            () -> {
                log.error("Previous pen not found.");
                return new ResourceNotFoundException("Method changePen: Previous pen not found.");
            }
        );

        Pen newPen = penRepository.findByName(newPenName).orElseThrow(
            () -> {
                log.error("New pen not found.");
                return new ResourceNotFoundException("Method changePen: New pen not found.");
            }
        );

        if (previousPen.getSpecies() != newPen.getSpecies() && !newPen.getAnimals().isEmpty()) {
            log.error("Pen already used.");
            throw new PenAlreadyUsedException("Method changePen: Pen already used.");
        } else {
            newPen.setSpecies(previousPen.getSpecies());
            newPen.setStatus("Active");
            previousPen.setStatus("Inactive");
            List<Animal> animals = previousPen.getAnimals();
            for (Animal animal : animals) {
                animal.setPen(newPen);
            }
        }
    }

    public List<PenDTO> getMap() {
        List<Pen> activePens = penRepository.findAll().stream().filter(pen -> (pen.getStatus().contains("active") || pen.getStatus().contains("maintenance"))).collect(Collectors.toList());
        Collections.sort(activePens);

        List<PenDTO> pens = new ArrayList<>();
        for (Pen activePen : activePens) {
            pens.add(PenDTO.builder()
                    .name(activePen.getName())
                    .location(activePen.getLocation())
                    .surface(activePen.getSurface())
                    .description(activePen.getDescription())
                    .speciesName(activePen.getSpecies().getName())
                    .build());
        }
        return pens;
    }

}
