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
        List<Diet> diets = dietRepository.findAllByPreferencesContaining("#" + food + "#");
        for (Diet diet : diets) {
            pens.add(diet.getAnimal().getPen());
        }
        return pens;
    }

    @Transactional
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

        if (!newPen.getAnimals().isEmpty()) {
            log.error("Pen already used.");
            throw new PenAlreadyUsedException("Method changePen: Pen already used.");
        } else {
            newPen.setSpecies(previousPen.getSpecies());
            newPen.setStatus(PenStatusType.active.name());
            previousPen.setStatus(PenStatusType.inactive.name());
            List<Animal> animals = previousPen.getAnimals();
            for (Animal animal : animals) {
                animal.setPen(newPen);
                animalRepository.save(animal);
            }
        }
        penRepository.save(previousPen);
        penRepository.save(newPen);
    }

    public List<PenDTO> getMap() {
        List<Pen> activePens = penRepository.findAll().stream()
                .filter(pen -> (pen.getStatus().equals(PenStatusType.active.name()) ||
                        pen.getStatus().equals(PenStatusType.maintenance.name())))
                .collect(Collectors.toList());

        Collections.sort(activePens);

        List<PenDTO> pens = new ArrayList<>();
        for (Pen activePen : activePens) {
            pens.add(PenMapper.mapToDto(activePen));
        }
        return pens;
    }

}
