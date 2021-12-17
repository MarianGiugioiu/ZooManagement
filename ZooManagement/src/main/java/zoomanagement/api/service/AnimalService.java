package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import zoomanagement.api.DTO.BabyAnimal;
import zoomanagement.api.DTO.GenealogicalTree;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.AnimalMissingInGenealogicalTreeException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.AnimalRepository;
import zoomanagement.api.repository.DietRepository;
import zoomanagement.api.repository.SpeciesRepository;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AnimalService implements ServiceInterface<Animal>{
    private final AnimalRepository animalRepository;
    private final SpeciesRepository speciesRepository;
    private final DietRepository dietRepository;

    @Override
    public List<Animal> getAll() {
        log.info("Fetching all animals...");
        return animalRepository.findAll();
    }

    @Override
    public Animal getOneById(UUID id) throws ResourceNotFoundException {
        log.info("Fetching animal with id {}...", id);
        return animalRepository.findById(id).orElse(null);
    }

    @Override
    public Animal add(Animal entry){
        log.info("Adding animal {}...", entry.getName());

        return animalRepository.save(entry);
    }

    @Override
    public Animal update(UUID id, Animal entry) throws ResourceNotFoundException{
        if(animalRepository.findById(id).isPresent()) {
            log.info("Updating animal with id {}...", id);
            entry.setId(id);
            return animalRepository.save(entry);
        }
        else {
            log.error("Animal not found in the database.");
            throw new ResourceNotFoundException("Method update: Animal not found.");
        }
    }

    @Override
    public void delete(UUID id) throws ResourceNotFoundException {
        if(animalRepository.findById(id).isPresent()) {
            log.info("Deleting animal with id {}...", id);
            animalRepository.deleteById(id);
        }
        else {
            log.error("Animal not found in the database.");
            throw new ResourceNotFoundException("Method delete: Animal not found.");
        }
    }

    public Animal getAnimalFromGenealogicalTree(GenealogicalTree genealogicalTree) throws AnimalMissingInGenealogicalTreeException, ResourceNotFoundException {
        Animal animal = animalRepository.findByName(genealogicalTree.getName()).orElseThrow(
            () -> {
                log.error("Animal not found.");
                return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Animal not found.");
            }
        );

        for (String parent : genealogicalTree.getParents()) {
            int index = parent.equals("female") ? 0 : 1;
            if (animal.getParents().get(index) == null) {
                throw new AnimalMissingInGenealogicalTreeException("Method getAnimalFromGenealogicalTree: Animal not found in genealogical tree.");
            } else {
                animal = animal.getParents().get(index);
            }
        }

        return animal;
    }

    public List<Animal> getAllAnimalsByAgeAndSexAndSpecies(String age, String sex, String speciesName) throws ResourceNotFoundException {
        Species species = speciesRepository.findByName(speciesName).orElseThrow(
            () -> {
                log.error("Species not found.");
                return new ResourceNotFoundException("Method getAllAnimalsByAgeAndSexAndSpecies: Species not found.");
            }
        );
        return animalRepository.findAllAnimalsByAgeAndSexAndSpecies(age, sex, species);
    }

    public HashMap<String, Animal> getUniqueAnimals() {
        HashMap<String, Animal> animalPerSpecies = new HashMap<>();
        List<Animal> animals = animalRepository.findAll();
        for (Animal animal: animals) {
            String speciesName = animal.getSpecies().getName();
            if (!animalPerSpecies.containsKey(speciesName)) {
                animalPerSpecies.put(speciesName, animal);
            } else {
                String previousAnimalPeculiarities = animalPerSpecies.get(speciesName).getPeculiarities();
                String newAnimalPeculiarities = animal.getPeculiarities();
                String[] previousAnimalPeculiaritiesList = previousAnimalPeculiarities.split(",");
                String[] newAnimalPeculiaritiesList = newAnimalPeculiarities.split(",");
                if(newAnimalPeculiaritiesList.length == previousAnimalPeculiaritiesList.length) {
                    if (previousAnimalPeculiarities.length() < newAnimalPeculiarities.length()) {
                        animalPerSpecies.put(speciesName, animal);
                    }
                } else if (newAnimalPeculiaritiesList.length > previousAnimalPeculiaritiesList.length) {
                    animalPerSpecies.put(speciesName, animal);
                }
            }
        }
        return  animalPerSpecies;
    }

    public Animal addBabyAnimal(BabyAnimal babyAnimal) throws ResourceNotFoundException {
        Animal mother = animalRepository.findByName(babyAnimal.getParents().get(0)).orElseThrow(
            () -> {
                log.error("Mother not found.");
                return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Mother not found.");
            }
        );

        Animal father = animalRepository.findByName(babyAnimal.getParents().get(1)).orElseThrow(
                () -> {
                    log.error("Father not found.");
                    return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Father not found.");
                }
        );

        Diet diet = dietRepository.save(Diet.builder()
                .recommendations(babyAnimal.getDiet().getRecommendations())
                .schedule(babyAnimal.getDiet().getSchedule())
                .preferences(babyAnimal.getDiet().getPreferences())
                .animal(null)
                .build());

        Animal animal = animalRepository.save(Animal.builder()
                .name(babyAnimal.getName())
                .age("0.1")
                .sex(babyAnimal.getSex())
                .species(mother.getSpecies())
                .pen(mother.getPen())
                .peculiarities(babyAnimal.getPeculiarities())
                .parents(new ArrayList<>(Arrays.asList(mother, father)))
                .children(new ArrayList<>())
                .diet(diet)
                .status("with mother")
                .build());

        diet.setAnimal(animal);
        dietRepository.save(diet);

        return animal;
    }

    public String getAnimalConditions (String animalName) throws ResourceNotFoundException {
        double optimalRatio = 0.75;
        Animal animal = animalRepository.findByName(animalName).orElseThrow(
            () -> {
                log.error("Animal not found.");
                return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Animal not found.");
            }
        );

        List<String> recommendedHabitatList = Arrays.asList(animal.getSpecies().getNaturalHabitat().split(","));
        List<String> currentHabitatList = Arrays.asList(animal.getPen().getDescription().split(","));
        List<String> recommendedDietList = Arrays.asList(animal.getDiet().getRecommendations().split(","));
        List<String> currentDietList = Arrays.asList(animal.getDiet().getPreferences().split(","));

        currentHabitatList.retainAll(recommendedHabitatList);
        currentDietList.retainAll(recommendedDietList);

        double habitatRatio = (double) currentHabitatList.size() / recommendedHabitatList.size();
        double dietRatio = (double) currentDietList.size() / recommendedDietList.size();

        if (habitatRatio < optimalRatio) {
            if (dietRatio < optimalRatio) {
                return "Both habitat and diet are not in optimal conditions.";
            } else {
                return "Habitat is not in optimal conditions.";
            }
        } else {
            if (dietRatio < optimalRatio) {
                return "Diet is not in optimal conditions.";
            } else {
                return "Both habitat and diet are in optimal conditions.";
            }
        }
    }
}
