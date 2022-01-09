package zoomanagement.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import zoomanagement.api.domain.Pen;
import zoomanagement.api.service.PenService;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.is;
import static zoomanagement.api.util.MockDataUtils.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void getAllPensWithAnimalsEating() throws Exception {
        String food = "honey";
        Pen pen = aPen("BearPublic");
        Set<Pen> pens = new HashSet<>();
        pens.add(pen);
        when(penService.getAllPensWithAnimalsEating(food)).thenReturn(pens);

        mockMvc.perform(get("/api/pen/food/" + food))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(pens)));
    }

    @Test
    void changePen() {
    }

    @Test
    void getMap() {
    }
}