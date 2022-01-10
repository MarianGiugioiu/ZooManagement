package zoomanagement.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import zoomanagement.api.DTO.BabyAnimal;
import zoomanagement.api.DTO.DietDTO;
import zoomanagement.api.DTO.GenealogicalTree;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Diet;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.AnimalMissingInGenealogicalTreeException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.service.AnimalService;
import zoomanagement.api.service.PenService;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static zoomanagement.api.util.MockDataUtils.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = AnimalController.class)
class AnimalControllerTest {
    @MockBean
    private AnimalService animalService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAnimalFromGenealogicalTreeStatusOk() throws Exception {
        GenealogicalTree genealogicalTree = aGenealogicalTree("Rex", new ArrayList<>(Arrays.asList("male", "female")));
        Animal animal = anAnimal("Rex");
        Animal father = anAnimal("Max", "male");
        Animal grandMother = anAnimal("Lucy", "female");
        Animal unknownAnimal = unknownAnimal();

        father.setParents(new ArrayList<>(Arrays.asList(grandMother, unknownAnimal)));
        animal.setParents(new ArrayList<>(Arrays.asList(unknownAnimal, father)));

        when(animalService.getAnimalFromGenealogicalTree(genealogicalTree)).thenReturn(animal);

        mockMvc.perform(get("/api/animals/genealogical_tree")
                .content(objectMapper.writeValueAsString(genealogicalTree))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.parents[1].name", is(father.getName())));
    }

    @Test
    void getAnimalFromGenealogicalTreeAnimalNotFound() throws Exception {
        GenealogicalTree genealogicalTree = aGenealogicalTree("Rex", new ArrayList<>(Arrays.asList("male", "female")));

        when(animalService.getAnimalFromGenealogicalTree(genealogicalTree)).thenThrow(new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Animal not found."));

        mockMvc.perform(get("/api/animals/genealogical_tree")
                .content(objectMapper.writeValueAsString(genealogicalTree))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAnimalFromGenealogicalTree: Animal not found.")));
    }

    @Test
    void getAnimalFromGenealogicalTreeAnimalMissingInGenealogicalTree() throws Exception {
        GenealogicalTree genealogicalTree = aGenealogicalTree("Rex", new ArrayList<>(Arrays.asList("male", "female")));

        when(animalService.getAnimalFromGenealogicalTree(genealogicalTree)).thenThrow(new AnimalMissingInGenealogicalTreeException("Method getAnimalFromGenealogicalTree: Animal not found in genealogical tree."));


        mockMvc.perform(get("/api/animals/genealogical_tree")
                .content(objectMapper.writeValueAsString(genealogicalTree))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAnimalFromGenealogicalTree: Animal not found in genealogical tree.")));
    }

    @Test
    void getAllAnimalsByAgeAndSexAndSpeciesStatusOk() throws Exception {
        Species species = aSpecies("Bear");
        Animal animal = anAnimal("Bruno");
        animal.setSpecies(species);
        List<Animal> animals = new ArrayList<>(Arrays.asList(animal));
        when(animalService.getAllAnimalsByAgeAndSexAndSpecies("12:3", "male", "Bear")).thenReturn(animals);

        mockMvc.perform(get("/api/animals/optional")
                .param("age", "12:3")
                .param("sex", "male")
                .param("speciesName", "Bear")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(animal.getName())))
                .andExpect(jsonPath("$[0].age", is(animal.getAge())))
                .andExpect(jsonPath("$[0].species.name", is(species.getName())));

    }

    @Test
    void getAllAnimalsByAgeAndSexAndSpeciesNotFound() throws Exception {
        when(animalService.getAllAnimalsByAgeAndSexAndSpecies("12:3", "female", "Bear")).thenThrow(new ResourceNotFoundException("Method getAllAnimalsByAgeAndSexAndSpecies: Species not found."));

        mockMvc.perform(get("/api/animals/optional")
                .param("age", "12:3")
                .param("sex", "female")
                .param("speciesName", "Bear")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAllAnimalsByAgeAndSexAndSpecies: Species not found.")));
    }

    @Test
    void getUniqueAnimalsStatusOk() throws Exception{
        Species bear = aSpecies("Bear");
        Species dog = aSpecies("Dog");
        Animal bruno = anAnimal("Bruno", "trait1,trait2", bear);
        Animal max = anAnimal("Max","long_trait1,trait2", dog);
        Map<String, Animal> animalPerSpeciesList = new HashMap<>();
        animalPerSpeciesList.put("Bear", bruno);
        animalPerSpeciesList.put("Dog", max);

        when(animalService.getUniqueAnimals()).thenReturn(animalPerSpeciesList);

        mockMvc.perform(get("/api/animals/unique"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", aMapWithSize(2)))
                .andExpect(jsonPath("$.Bear.name", is(bruno.getName())))
                .andExpect(jsonPath("$.Dog.name", is(max.getName())));
    }

    @Test
    void addBabyAnimalStatusCreated() throws Exception{
        Species species = aSpecies("Dog");
        Animal mother = anAnimal("Lucy", "female");
        mother.setSpecies(species);
        Animal father = anAnimal("Max", "male");
        father.setSpecies(species);
        BabyAnimal babyAnimal = aBabyAnimal("Rex", "Lucy", "Max");
        DietDTO dietDTO = aDietDTO("#honey#", "#honey#");
        babyAnimal.setDiet(dietDTO);
        Diet diet = Diet.builder()
                .recommendations(babyAnimal.getDiet().getRecommendations())
                .schedule(babyAnimal.getDiet().getSchedule())
                .preferences(babyAnimal.getDiet().getPreferences())
                .animal(null)
                .build();

        Animal animal = Animal.builder()
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
                .build();

        when(animalService.addBabyAnimal(babyAnimal)).thenReturn(animal);

        mockMvc.perform(post("/api/animals/baby")
                .content(objectMapper.writeValueAsString(babyAnimal))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name", is(animal.getName())))
                .andExpect(jsonPath("$.age", is(animal.getAge())))
                .andExpect(jsonPath("$.species.name", is(species.getName())))
                .andExpect(jsonPath("$.parents[0].name", is(mother.getName())))
                .andExpect(jsonPath("$.diet.preferences", is(dietDTO.getPreferences())));

    }

    @Test
    void addBabyAnimalMotherNotFound() throws Exception {
        BabyAnimal babyAnimal = aBabyAnimal("Rex", "Lucy", "Max");
        DietDTO diet = aDietDTO("#honey#", "#honey#");
        babyAnimal.setDiet(diet);
        when(animalService.addBabyAnimal(babyAnimal)).thenThrow(new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Mother not found."));

        mockMvc.perform(post("/api/animals/baby")
                .content(objectMapper.writeValueAsString(babyAnimal))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAnimalFromGenealogicalTree: Mother not found.")));
    }

    @Test
    void addBabyAnimalFatherNotFound() throws Exception {
        BabyAnimal babyAnimal = aBabyAnimal("Rex", "Lucy", "Max");
        DietDTO diet = aDietDTO("#honey#", "#honey#");
        babyAnimal.setDiet(diet);
        when(animalService.addBabyAnimal(babyAnimal)).thenThrow(new ResourceNotFoundException("Method getAnimalFromGenealogicalTree: Father not found."));

        mockMvc.perform(post("/api/animals/baby")
                .content(objectMapper.writeValueAsString(babyAnimal))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAnimalFromGenealogicalTree: Father not found.")));
    }

    @Test
    void getAnimalConditionsStatusOk() throws Exception {
        String name = "Bruno";
        when(animalService.getAnimalConditions(name)).thenReturn("Both habitat and diet are not in optimal conditions.");

        MockHttpServletResponse response = mockMvc.perform(get("/api/animals/conditions/" + name))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andReturn().getResponse();

        assertEquals("Both habitat and diet are not in optimal conditions.", response.getContentAsString());
    }

    @Test
    void getAnimalConditionsAnimalNotFound() throws Exception {
        String name = "Bruno";
        when(animalService.getAnimalConditions(name)).thenThrow(new ResourceNotFoundException("Method getAnimalConditions: Animal not found."));

        mockMvc.perform(get("/api/animals/conditions/" + name))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method getAnimalConditions: Animal not found.")));
    }
}