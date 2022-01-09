package zoomanagement.api.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import zoomanagement.api.DTO.BabyAnimal;
import zoomanagement.api.DTO.GenealogicalTree;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.AnimalMissingInGenealogicalTreeException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.repository.AnimalRepository;
import zoomanagement.api.repository.SpeciesRepository;

import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static zoomanagement.api.util.MockDataUtils.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AnimalServiceTest {
    @InjectMocks
    private AnimalService animalService;

    @Mock
    private AnimalRepository animalRepository;

    @Mock
    private SpeciesRepository speciesRepository;

    @Test
    void getAnimalFromGenealogicalTreeResultFound() throws AnimalMissingInGenealogicalTreeException, ResourceNotFoundException {
        //Arrange
        GenealogicalTree genealogicalTree = aGenealogicalTree("Rex", new ArrayList<>(Arrays.asList("male", "female")));
        Animal animal = anAnimal("Rex");
        Animal father = anAnimal("Max", "male");
        Animal grandMother = anAnimal("Lucy", "female");

        father.setParents(new ArrayList<>(Arrays.asList(grandMother, null)));
        animal.setParents(new ArrayList<>(Arrays.asList(null, father)));

        when(animalRepository.findByName("Rex")).thenReturn(Optional.of(animal));

        //Act
        Animal result = animalService.getAnimalFromGenealogicalTree(genealogicalTree);

        //Assert
        assertEquals(grandMother, result);
    }

    @Test
    void getAnimalFromGenealogicalTreeAnimalNotFound() {
        //Arrange
        GenealogicalTree genealogicalTree = aGenealogicalTree("Bruno", null);
        when(animalRepository.findByName(anyString())).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> animalService.getAnimalFromGenealogicalTree(genealogicalTree))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method getAnimalFromGenealogicalTree: Animal not found.");
    }

    @Test
    void getAnimalFromGenealogicalTreeAnimalMissingInGenealogicalTree() {
        //Arrange
        GenealogicalTree genealogicalTree = aGenealogicalTree("Rex", new ArrayList<>(Arrays.asList("male")));
        Animal animal = anAnimal("Rex");


        when(animalRepository.findByName("Rex")).thenReturn(Optional.of(animal));

        //Act
        assertThatThrownBy(() -> animalService.getAnimalFromGenealogicalTree(genealogicalTree))
                .isInstanceOf(AnimalMissingInGenealogicalTreeException.class)
                .hasMessageContaining("Method getAnimalFromGenealogicalTree: Animal not found in genealogical tree.");
    }

    @Test
    void getAllAnimalsByAgeAndSexAndSpeciesResultFound() throws ResourceNotFoundException {
        //Arrange
        Species species = aSpecies("Bear");
        Animal animal = anAnimal("Bruno");
        animal.setSpecies(species);
        List<Animal> animals = new ArrayList<>(Arrays.asList(animal));

        when(speciesRepository.findByName("Bear")).thenReturn(Optional.of(species));
        when(animalRepository.findAllAnimalsByAgeAndSexAndSpecies("12:3", "male", species))
                .thenReturn(animals);

        //Act
        List<Animal> result = animalService.getAllAnimalsByAgeAndSexAndSpecies("12:3", "male", "Bear");

        //Assert
        assertEquals(animals, result);
    }

    @Test
    void getAllAnimalsByAgeAndSexAndSpeciesSpeciesMissing() {
        //Arrange
        when(speciesRepository.findByName(anyString())).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> animalService.getAllAnimalsByAgeAndSexAndSpecies("12:3", "male", "Bear"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method getAllAnimalsByAgeAndSexAndSpecies: Species not found.");
    }

    @Test
    void getUniqueAnimals() {
        //Arrange
        Species bear = aSpecies("Bear");
        Species dog = aSpecies("Dog");
        Animal bruno = anAnimal("Bruno", "trait1,trait2", bear);
        Animal teddy = anAnimal("Teddy","trait1", bear);
        Animal rex = anAnimal("Rex","trait1,trait2", dog);
        Animal max = anAnimal("Max","long_trait1,trait2", dog);

        List<Animal> animals = new ArrayList<>(Arrays.asList(bruno, teddy, rex, max));
        Map<String, Animal> animalPerSpeciesList = new HashMap<>();
        animalPerSpeciesList.put("Bear", bruno);
        animalPerSpeciesList.put("Dog", max);

        when(animalRepository.findAll()).thenReturn(animals);

        //Act
        Map<String, Animal> result = animalService.getUniqueAnimals();

        //Assert
        assertEquals(animalPerSpeciesList, result);
    }

    @Test
    void addBabyAnimal() {
    }

    @Test
    void addBabyAnimalMotherNotFound() {
        //Arrange
        BabyAnimal babyAnimal = aBabyAnimal("Rex", "Lucy", "Max");
        when(animalRepository.findByName(anyString())).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> animalService.addBabyAnimal(babyAnimal))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method getAnimalFromGenealogicalTree: Mother not found.");
    }

    @Test
    void addBabyAnimalFatherNotFound() {
        //Arrange
        BabyAnimal babyAnimal = aBabyAnimal("Rex", "Lucy", "Max");
        Animal mother = anAnimal("Lucy", "female");
        when(animalRepository.findByName("Lucy")).thenReturn(Optional.of(mother));
        when(animalRepository.findByName("Max")).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> animalService.addBabyAnimal(babyAnimal))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method getAnimalFromGenealogicalTree: Father not found.");
    }

    @Test
    void getAnimalConditionsFirstCase() throws ResourceNotFoundException {
        //Arrange
        Pen pen = aPen("BearPublic", "#property1#");
        Species species = aSpecies("Bear", "#property2#");
        Diet diet = aDiet("#food1#,#food2#", "#food1#,#food3#");
        Animal animal = anAnimal("Bruno", diet, species, pen);

        String expectedResult = "Both habitat and diet are not in optimal conditions.";

        when(animalRepository.findByName("Bruno")).thenReturn(Optional.of(animal));

        //Act
        String result = animalService.getAnimalConditions("Bruno");

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getAnimalConditionsSecondCase() throws ResourceNotFoundException {
        //Arrange
        Pen pen = aPen("BearPublic", "#property1#");
        Species species = aSpecies("Bear", "#property2#");
        Diet diet = aDiet("#food1#,#food2#", "#food1#");
        Animal animal = anAnimal("Bruno", diet, species, pen);

        String expectedResult = "Habitat is not in optimal conditions.";

        when(animalRepository.findByName("Bruno")).thenReturn(Optional.of(animal));

        //Act
        String result = animalService.getAnimalConditions("Bruno");

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getAnimalConditionsThirdCase() throws ResourceNotFoundException {
        //Arrange
        Pen pen = aPen("BearPublic", "#property1#");
        Species species = aSpecies("Bear", "#property1#");
        Diet diet = aDiet("#food1#,#food2#", "#food1#,#food3#");
        Animal animal = anAnimal("Bruno", diet, species, pen);

        String expectedResult = "Diet is not in optimal conditions.";

        when(animalRepository.findByName("Bruno")).thenReturn(Optional.of(animal));

        //Act
        String result = animalService.getAnimalConditions("Bruno");

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getAnimalConditionsLastCase() throws ResourceNotFoundException {
        //Arrange
        Pen pen = aPen("BearPublic", "#property1#");
        Species species = aSpecies("Bear", "#property1#");
        Diet diet = aDiet("#food1#,#food2#", "#food1#");
        Animal animal = anAnimal("Bruno", diet, species, pen);

        String expectedResult = "Both habitat and diet are in optimal conditions.";

        when(animalRepository.findByName("Bruno")).thenReturn(Optional.of(animal));

        //Act
        String result = animalService.getAnimalConditions("Bruno");

        //Assert
        assertEquals(expectedResult, result);
    }

    @Test
    void getAnimalConditionsAnimalNotFound() {
        when(animalRepository.findByName(anyString())).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> animalService.getAnimalConditions("Bruno"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method getAnimalConditions: Animal not found.");
    }
}