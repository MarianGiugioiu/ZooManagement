package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.domain.PenStatusType;
import zoomanagement.api.exception.PenAlreadyUsedException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.mapper.PenMapper;
import zoomanagement.api.repository.AnimalRepository;
import zoomanagement.api.repository.DietRepository;
import zoomanagement.api.repository.PenRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PenService{
    private final PenRepository penRepository;
    private final DietRepository dietRepository;
    private final AnimalRepository animalRepository;

    public Set<Pen> getAllPensWithAnimalsEating(String food) {
        Set<Pen> pens = new HashSet<>();
        //Searches for all diets which contain "#food#" in the string of preferences
        List<Diet> diets = dietRepository.findAllByPreferencesContaining("#" + food + "#");
        //Gets the pen of the animal having the diet for each diet
        for (Diet diet : diets) {
            pens.add(diet.getAnimal().getPen());
        }
        return pens;
    }

    @Transactional
    public Pen changePen(String previousPenName, String newPenName) throws ResourceNotFoundException, PenAlreadyUsedException {
        //Searches for previous pen
        Pen previousPen = penRepository.findByName(previousPenName).orElseThrow(
            () -> {
                log.error("Previous pen not found.");
                return new ResourceNotFoundException("Method changePen: Previous pen not found.");
            }
        );

        //Searches for new pen
        Pen newPen = penRepository.findByName(newPenName).orElseThrow(
            () -> {
                log.error("New pen not found.");
                return new ResourceNotFoundException("Method changePen: New pen not found.");
            }
        );

        //Gets the list of animals from the previous pen and updates the pen
        List<Animal> animalsBeforeChange = previousPen.getAnimals();
        Pen newPenChanged = null;

        List<Animal> animalsChanged = new ArrayList<>();

        //Checks if the new pen is empty
        if (!newPen.getAnimals().isEmpty()) {
            log.error("Pen already used.");
            throw new PenAlreadyUsedException("Method changePen: Pen already used.");
        } else {
            ////Updates the pens
            previousPen.setAnimals(new ArrayList<>());
            previousPen.setStatus(PenStatusType.inactive.name());
            Pen previousPenChanged = penRepository.save(previousPen);

            newPen.setSpecies(previousPen.getSpecies());
            newPen.setStatus(PenStatusType.active.name());
            newPenChanged = penRepository.save(newPen);

            //Adds every modified animal to the list of changed animals
            for (Animal animal : animalsBeforeChange) {
                animal.setPen(newPenChanged);
                animalRepository.save(animal);
                animalsChanged.add(animal);
            }
        }

        //System.out.println(animalsChanged.toString());
        System.out.println(newPenChanged);
        //Sets the list of animals to the new pen
        newPenChanged.setAnimals(animalsChanged);
        return newPenChanged;
    }

    public List<PenDTO> getMap() {
        //Searches for all pens with status "active" or "maintenance"
        List<Pen> activePens = penRepository.findAll().stream()
                .filter(pen -> (pen.getStatus().equals(PenStatusType.active.name()) ||
                        pen.getStatus().equals(PenStatusType.maintenance.name())))
                .collect(Collectors.toList());

        //Sorts the list by location (a string containing the 2D coordinates of the pen relative to the (0,0) point)
        //The comparison is based on the distance from the pen to the (0,0) point
        Collections.sort(activePens);

        //Creates a PenDTO with enough information for each filtered pen
        List<PenDTO> pens = new ArrayList<>();
        for (Pen activePen : activePens) {
            pens.add(PenMapper.mapToDto(activePen));
        }
        return pens;
    }

}
