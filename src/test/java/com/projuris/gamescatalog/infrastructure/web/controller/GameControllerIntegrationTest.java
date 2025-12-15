package com.projuris.gamescatalog.infrastructure.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projuris.gamescatalog.application.dto.GameRequestDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class GameControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateGameSuccessfully() throws Exception {
        GameRequestDTO request = new GameRequestDTO(
                "The Witcher 3",
                "RPG de mundo aberto",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                99.90);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("The Witcher 3"))
                .andExpect(jsonPath("$.developer").value("CD Projekt RED"))
                .andExpect(jsonPath("$.genre").value("RPG"));
    }

    @Test
    void shouldReturnBadRequestWhenInvalidData() throws Exception {
        GameRequestDTO request = new GameRequestDTO(
                "",
                "Descrição",
                "Developer",
                "Publisher",
                "Genre",
                2015,
                99.90);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldGetGameByIdSuccessfully() throws Exception {
        GameRequestDTO createRequest = new GameRequestDTO(
                "Cyberpunk 2077",
                "RPG futurista",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2020,
                199.90);

        String responseBody = mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long gameId = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(get("/api/games/{id}", gameId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Cyberpunk 2077"));
    }

    @Test
    void shouldReturnNotFoundWhenGameDoesNotExist() throws Exception {
        mockMvc.perform(get("/api/games/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListAllGames() throws Exception {
        GameRequestDTO game1 = new GameRequestDTO(
                "Game 1", "Desc 1", "Dev 1", "Pub 1", "Action", 2020, 50.0);
        GameRequestDTO game2 = new GameRequestDTO(
                "Game 2", "Desc 2", "Dev 2", "Pub 2", "RPG", 2021, 60.0);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game1)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(game2)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void shouldUpdateGameSuccessfully() throws Exception {
        GameRequestDTO createRequest = new GameRequestDTO(
                "Original Game", "Original Desc", "Dev 1", "Pub 1", "Action", 2020, 50.0);

        String responseBody = mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long gameId = objectMapper.readTree(responseBody).get("id").asLong();

        GameRequestDTO updateRequest = new GameRequestDTO(
                "Updated Game", "Updated Desc", "Dev 2", "Pub 2", "RPG", 2021, 60.0);

        mockMvc.perform(put("/api/games/{id}", gameId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Game"))
                .andExpect(jsonPath("$.developer").value("Dev 2"))
                .andExpect(jsonPath("$.genre").value("RPG"));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentGame() throws Exception {
        GameRequestDTO updateRequest = new GameRequestDTO(
                "Updated Game", "Updated Desc", "Dev 2", "Pub 2", "RPG", 2021, 60.0);

        mockMvc.perform(put("/api/games/{id}", 999L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldDeleteGameSuccessfully() throws Exception {
        GameRequestDTO createRequest = new GameRequestDTO(
                "Game to Delete", "Desc", "Dev", "Pub", "Action", 2020, 50.0);

        String responseBody = mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Long gameId = objectMapper.readTree(responseBody).get("id").asLong();

        mockMvc.perform(delete("/api/games/{id}", gameId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/games/{id}", gameId))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentGame() throws Exception {
        mockMvc.perform(delete("/api/games/{id}", 999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldListGamesByGenre() throws Exception {
        GameRequestDTO rpgGame = new GameRequestDTO(
                "RPG Game", "Desc", "Dev", "Pub", "RPG", 2020, 50.0);
        GameRequestDTO actionGame = new GameRequestDTO(
                "Action Game", "Desc", "Dev", "Pub", "Action", 2021, 60.0);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(rpgGame)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(actionGame)))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/games").param("genre", "RPG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].genre").value("RPG"))
                .andExpect(jsonPath("$.page").exists())
                .andExpect(jsonPath("$.size").exists())
                .andExpect(jsonPath("$.totalElements").exists());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidPrice() throws Exception {
        GameRequestDTO request = new GameRequestDTO(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                "Valid Publisher",
                "Valid Genre",
                2020,
                -10.0);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnBadRequestWhenInvalidReleaseYear() throws Exception {
        GameRequestDTO request = new GameRequestDTO(
                "Valid Title",
                "Valid Description",
                "Valid Developer",
                "Valid Publisher",
                "Valid Genre",
                1800,
                50.0);

        mockMvc.perform(post("/api/games")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
