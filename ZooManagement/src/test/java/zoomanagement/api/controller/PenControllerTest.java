package zoomanagement.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import zoomanagement.api.DTO.PenDTO;
import zoomanagement.api.domain.Animal;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.domain.PenStatusType;
import zoomanagement.api.domain.Species;
import zoomanagement.api.exception.ExceptionResponse;
import zoomanagement.api.exception.PenAlreadyUsedException;
import zoomanagement.api.exception.ResourceNotFoundException;
import zoomanagement.api.mapper.PenMapper;
import zoomanagement.api.service.PenService;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static zoomanagement.api.util.MockDataUtils.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = PenController.class)
class PenControllerTest {
    @MockBean
    private PenService penService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllPensWithAnimalsEatingStatusOk() throws Exception {
        String food = "honey";
        Pen pen = aPen("BearPublic");
        Set<Pen> pens = new HashSet<>();
        pens.add(pen);
        when(penService.getAllPensWithAnimalsEating(food)).thenReturn(pens);


        mockMvc.perform(get("/api/pens/food/" + food))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(pen.getName())));
    }

    @Test
    void changePenSuccess() throws Exception {
        Species species = aSpecies("Bear");
        Pen pen2 = aPen("BearPublic2");
        Animal animal1 = anAnimal("Bruno");
        Animal animal2 = anAnimal("Teddy");

        animal1.setId(UUID.randomUUID());
        animal2.setId(UUID.randomUUID());
        pen2.setId(UUID.randomUUID());
        pen2.setStatus("active");
        animal1.setPen(pen2);
        animal2.setSpecies(species);
        animal2.setPen(pen2);
        pen2.setSpecies(species);

        pen2.setAnimals(new ArrayList<>(Arrays.asList(animal1, animal2)));
        when(penService.changePen("BearPublic1", "BearPublic2")).thenReturn(pen2);

        //System.out.println(pen2.getSpecies());

        mockMvc.perform(patch("/api/pens/change")
                .param("previousPen", "BearPublic1")
                .param("newPen", "BearPublic2")
        )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("name", is(pen2.getName())))
                .andExpect(jsonPath("$.animals[0].name", is(animal1.getName())))
                .andExpect(jsonPath("$.animals[1].name", is(animal2.getName())))
                .andExpect(jsonPath("species.name", is(species.getName())))
                .andExpect(jsonPath("status", is("active")));

    }

    @Test
    void changePenPreviousPenNotFound() throws Exception {
        when(penService.changePen("pen1", "pen2")).thenThrow(new ResourceNotFoundException("Method changePen: Previous pen not found."));

        mockMvc.perform(patch("/api/pens/change")
                .param("previousPen", "pen1")
                .param("newPen", "pen2")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method changePen: Previous pen not found.")));
    }

    @Test
    void changePenNewPenNotFound() throws Exception {
        when(penService.changePen("pen1", "pen2")).thenThrow(new ResourceNotFoundException("Method changePen: New pen not found."));

        mockMvc.perform(patch("/api/pens/change")
                .param("previousPen", "pen1")
                .param("newPen", "pen2")
        )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("message", is("Method changePen: New pen not found.")));
    }

    @Test
    void changePenAlreadyUsed() throws Exception {
        when(penService.changePen("pen1", "pen2")).thenThrow(new PenAlreadyUsedException("Method changePen: Pen already used."));

        mockMvc.perform(patch("/api/pens/change")
                .param("previousPen", "pen1")
                .param("newPen", "pen2")
        )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("message", is("Method changePen: Pen already used.")));
    }

    @Test
    void getMap() throws Exception {
        Species species = aSpecies("Dog");

        Pen pen2 = aPen("pen2", "11:5", PenStatusType.active.name(), species);
        Pen pen3 = aPen("pen3", "11:3", PenStatusType.maintenance.name(), species);

        PenDTO penDTO1 = PenMapper.mapToDto(pen3);
        PenDTO penDTO2 = PenMapper.mapToDto(pen2);
        List<PenDTO> penDTOList = new ArrayList<>();
        penDTOList.add(penDTO1);
        penDTOList.add(penDTO2);

        when(penService.getMap()).thenReturn(penDTOList);

        mockMvc.perform(get("/api/pens/map"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is(penDTO1.getName())))
                .andExpect(jsonPath("$[1].name", is(penDTO2.getName())));
    }
}