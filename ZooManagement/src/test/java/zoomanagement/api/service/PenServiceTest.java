package zoomanagement.api.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.*;
import zoomanagement.api.exception.PenAlreadyUsedException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.mapper.PenMapper;
import zoomanagement.api.repository.AnimalRepository;
import zoomanagement.api.repository.DietRepository;
import zoomanagement.api.repository.PenRepository;
import static zoomanagement.api.util.MockDataUtils.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PenServiceTest {
    @InjectMocks
    private PenService penService;

    @Mock
    private PenRepository penRepository;
    @Mock
    private DietRepository dietRepository;
    @Mock
    private AnimalRepository animalRepository;

    @Test
    @DisplayName("Test getAllPensWithAnimalsEating returns correct pens.")
    void getAllPensWithAnimalsEatingResultFound() {
        //Arrange
        Pen pen = aPen("BearPublic");
        Animal animal = anAnimal("Bruno");
        Diet diet = aDiet("#honey#");

        animal.setPen(pen);
        diet.setAnimal(animal);

        Set<Pen> pens = new HashSet<>();
        pens.add(pen);

        List<Diet> diets= new ArrayList<>();
        diets.add(diet);

        when(dietRepository.findAllByPreferencesContaining("#honey#"))
                .thenReturn(diets);

        //Act
        Set<Pen> result = penService.getAllPensWithAnimalsEating("honey");

        //Assert
        assertEquals(pens, result);
    }

    @Test
    @DisplayName("Test getAllPensWithAnimalsEating returns no pens.")
    void getAllPensWithAnimalsEatingEmptyResult() {
        //Arrange
        when(dietRepository.findAllByPreferencesContaining("#meat#"))
                .thenReturn(new ArrayList<>());

        //Act
        Set<Pen> result = penService.getAllPensWithAnimalsEating("meat");

        //Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void changePen() throws ResourceNotFoundException, PenAlreadyUsedException {
        //Arrange
        Species species = aSpecies("Bear");
        Pen pen1 = aPen("BearPublic1");
        Pen pen2 = aPen("BearPublic2");
        Animal animal = anAnimal("Bruno");
        pen1.setSpecies(species);
        pen2.setStatus("inactive");
        animal.setSpecies(species);
        animal.setPen(pen1);
        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        pen1.setAnimals(animals);
        Animal savedAnimal = anAnimal("Bruno");
        savedAnimal.setId(UUID.randomUUID());

        when(penRepository.findByName("BearPublic1")).thenReturn(Optional.of(pen1));
        when(penRepository.findByName("BearPublic2")).thenReturn(Optional.of(pen2));
        when(animalRepository.save(animal)).thenReturn(savedAnimal);

        //Act
        penService.changePen("BearPublic1", "BearPublic2");

        //Assert

    }

    @Test
    void changePenPreviousPenNotFound(){
        //Arrange
        when(penRepository.findByName(anyString())).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> penService.changePen("BearPublic1", "BearPublic2"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method changePen: Previous pen not found.");
    }

    @Test
    void changePenNewPenNotFound(){
        //Arrange
        when(penRepository.findByName("BearPublic1")).thenReturn(Optional.of(new Pen()));
        when(penRepository.findByName("BearPublic2")).thenReturn(Optional.empty());

        //Act
        assertThatThrownBy(() -> penService.changePen("BearPublic1", "BearPublic2"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Method changePen: New pen not found.");
    }

    @Test
    void changePenAlreadyUsed(){
        //Arrange
        Pen newPen = new Pen();
        Animal animal = new Animal();
        List<Animal> animals = new ArrayList<>();
        animals.add(animal);
        newPen.setAnimals(animals);
        when(penRepository.findByName("BearPublic1")).thenReturn(Optional.of(new Pen()));
        when(penRepository.findByName("BearPublic2")).thenReturn(Optional.of(newPen));

        //Act
        assertThatThrownBy(() -> penService.changePen("BearPublic1", "BearPublic2"))
                .isInstanceOf(PenAlreadyUsedException.class)
                .hasMessageContaining("Method changePen: Pen already used.");
    }

    @Test
    void getMap() {
        //Arrange
        Species species = aSpecies("Dog");

        Pen pen1 = aPen("Jasper", "", PenStatusType.inactive.name(), null);
        Pen pen2 = aPen("Rex", "11:5", PenStatusType.active.name(), species);
        Pen pen3 = aPen("Max", "11:3", PenStatusType.maintenance.name(), species);
        List<Pen> pens = new ArrayList<>();
        pens.add(pen1);
        pens.add(pen2);
        pens.add(pen3);

        PenDTO penDTO1 = PenMapper.mapToDto(pen3);
        PenDTO penDTO2 = PenMapper.mapToDto(pen2);
        List<PenDTO> penDTOList = new ArrayList<>();
        penDTOList.add(penDTO1);
        penDTOList.add(penDTO2);

        when(penRepository.findAll()).thenReturn(pens);

        //Act
        List<PenDTO> result = penService.getMap();

        //Assert
        assertEquals(penDTOList, result);
    }
}