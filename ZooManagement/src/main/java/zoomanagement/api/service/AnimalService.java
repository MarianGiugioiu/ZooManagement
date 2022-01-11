package zoomanagement.api.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
public class AnimalService{
    private final AnimalRepository animalRepository;
    private final SpeciesRepository speciesRepository;
    private final DietRepository dietRepository;

    public Animal getAnimalFromGenealogicalTree(GenealogicalTree genealogicalTree) throws AnimalMissingInGenealogicalTreeException, ResourceNotFoundException {
        //Searches the first animal in the genealogical tree
        Animal animal = animalRepository.findByName(genealogicalTree.getName()).orElseThrow(
            () -> {
                log.error("Animal not found.");
                return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Animal not found.");
            }
        );

        //Searches for every parent in the genealogical tree
        //Stops when a parent is unknown
        for (String parent : genealogicalTree.getParents()) {
            int index = parent.equals("female") ? 0 : 1;
            if (animal.getParents().get(index).getName().equals("Unknown")) {
                throw new AnimalMissingInGenealogicalTreeException("Method getAnimalFromGenealogicalTree: Animal not found in genealogical tree.");
            } else {
                animal = animal.getParents().get(index);
            }
        }

        return animal;
    }

    public List<Animal> getAllAnimalsByAgeAndSexAndSpecies(String age, String sex, String speciesName) throws ResourceNotFoundException {
        //Searches for species

        Species species = null;
        if (speciesName != null) {
            species = speciesRepository.findByName(speciesName).orElseThrow(
                    () -> {
                        log.error("Species not found.");
                        return new ResourceNotFoundException("Method getAllAnimalsByAgeAndSexAndSpecies: Species not found.");
                    }
            );
        }
        //Searches for animals with the corresponding optional parameters
        //If a parameter is null, it's omitted
        return animalRepository.findAllAnimalsByAgeAndSexAndSpecies(age, sex, species);
    }

    public Map<String, Animal> getUniqueAnimals() {
        HashMap<String, Animal> animalPerSpecies = new HashMap<>();
        List<Animal> animals = animalRepository.findAll();
        //Filters all animals
        for (Animal animal: animals) {
            if (animal.getSpecies()==null) {
                continue;
            }
            String speciesName = animal.getSpecies().getName();
            //If the animal is the first of its species, add it to the map
            if (!animalPerSpecies.containsKey(speciesName)) {
                animalPerSpecies.put(speciesName, animal);
            } else {
                //Else, checks if the current animal has more or larger peculiarities than the saved animal for the current species
                //Peculiarities is a string like: "trait1,trait2"
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

    @Transactional
    public Animal addBabyAnimal(BabyAnimal babyAnimal) throws ResourceNotFoundException {
        //Searches for mother of the given animal
        Animal mother = animalRepository.findByName(babyAnimal.getParents().get(0)).orElseThrow(
            () -> {
                log.error("Mother not found.");
                return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Mother not found.");
            }
        );
        //Searches for father of the given animal
        Animal father = animalRepository.findByName(babyAnimal.getParents().get(1)).orElseThrow(
                () -> {
                    log.error("Father not found.");
                    return new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Father not found.");
                }
        );

        //Creates a diet based on the DTO
        Diet diet = dietRepository.save(Diet.builder()
                .recommendations(babyAnimal.getDiet().getRecommendations())
                .schedule(babyAnimal.getDiet().getSchedule())
                .preferences(babyAnimal.getDiet().getPreferences())
                .animal(null)
                .build());

        //Creates an animal based on the above information
        Animal animal = animalRepository.save(Animal.builder()
                .name(babyAnimal.getName())
                .age("0:1")
                .sex(babyAnimal.getSex())
                .species(mother.getSpecies())
                .pen(mother.getPen())
                .peculiarities(babyAnimal.getPeculiarities())
                .parents(new ArrayList<>(Arrays.asList(mother, father)))
                .children(new ArrayList<>())
                .diet(diet)
                .status("with mother")
                .build());

        return animal;
    }

    public String getAnimalConditions (String animalName) throws ResourceNotFoundException {
        double optimalRatio = 0.75;
        //Searches for the animal
        Animal animal = animalRepository.findByName(animalName).orElseThrow(
            () -> {
                log.error("Animal not found.");
                return new ResourceNotFoundException("Method getAnimalConditions: Animal not found.");
            }
        );

        List<String> recommendedHabitatList = new ArrayList<>(Arrays.asList(animal.getSpecies().getNaturalHabitat().split(",")));
        List<String> currentHabitatList = new ArrayList<>(Arrays.asList(animal.getPen().getDescription().split(",")));
        List<String> recommendedDietList = new ArrayList<>(Arrays.asList(animal.getDiet().getRecommendations().split(",")));
        List<String> currentDietList = new ArrayList<>(Arrays.asList(animal.getDiet().getPreferences().split(",")));

        //Retains in the habitat of the pen, the traits of the natural habitat
        currentHabitatList.retainAll(recommendedHabitatList);
        //Retains in the food preferences, the food in the recommendations
        currentDietList.retainAll(recommendedDietList);

        //The ratio between current and recommended needs to be higher or equal to the optimalRation
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
