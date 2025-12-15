package com.projuris.gamescatalog.application.usecase;

import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.application.mapper.GameMapper;
import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListGamesByGenreUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private ListGamesByGenreUseCase listGamesByGenreUseCase;

    private List<Game> rpgGames;
    private List<GameResponseDTO> rpgGameResponseDTOs;

    @BeforeEach
    void setUp() {
        rpgGames = Arrays.asList(
                Game.reconstruct(1L, "RPG Game 1", "Desc 1", "Dev 1", "Pub 1", "RPG", 2020, 50.0,
                        LocalDateTime.now(), LocalDateTime.now()),
                Game.reconstruct(2L, "RPG Game 2", "Desc 2", "Dev 2", "Pub 2", "RPG", 2021, 60.0,
                        LocalDateTime.now(), LocalDateTime.now()));

        rpgGameResponseDTOs = Arrays.asList(
                new GameResponseDTO(1L, "RPG Game 1", "Desc 1", "Dev 1", "Pub 1", "RPG", 2020, 50.0,
                        LocalDateTime.now(), LocalDateTime.now()),
                new GameResponseDTO(2L, "RPG Game 2", "Desc 2", "Dev 2", "Pub 2", "RPG", 2021, 60.0,
                        LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void shouldListGamesByGenreSuccessfully() {
        String genre = "RPG";
        when(gameRepository.findByGenre(genre)).thenReturn(rpgGames);
        when(gameMapper.toResponseDTO(rpgGames.get(0))).thenReturn(rpgGameResponseDTOs.get(0));
        when(gameMapper.toResponseDTO(rpgGames.get(1))).thenReturn(rpgGameResponseDTOs.get(1));

        List<GameResponseDTO> result = listGamesByGenreUseCase.execute(genre);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("RPG Game 1", result.get(0).getTitle());
        assertEquals("RPG Game 2", result.get(1).getTitle());
        assertEquals("RPG", result.get(0).getGenre());
        assertEquals("RPG", result.get(1).getGenre());

        verify(gameRepository, times(1)).findByGenre(genre);
        verify(gameMapper, times(2)).toResponseDTO(any(Game.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoGamesForGenre() {
        String genre = "Horror";
        when(gameRepository.findByGenre(genre)).thenReturn(List.of());

        List<GameResponseDTO> result = listGamesByGenreUseCase.execute(genre);

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(gameRepository, times(1)).findByGenre(genre);
        verify(gameMapper, never()).toResponseDTO(any());
    }
}
