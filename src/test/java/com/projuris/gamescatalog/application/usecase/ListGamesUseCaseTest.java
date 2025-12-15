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
class ListGamesUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private ListGamesUseCase listGamesUseCase;

    private List<Game> games;
    private List<GameResponseDTO> gameResponseDTOs;

    @BeforeEach
    void setUp() {
        games = Arrays.asList(
                Game.reconstruct(1L, "Game 1", "Desc 1", "Dev 1", "Pub 1", "Action", 2020, 50.0,
                        LocalDateTime.now(), LocalDateTime.now()),
                Game.reconstruct(2L, "Game 2", "Desc 2", "Dev 2", "Pub 2", "RPG", 2021, 60.0,
                        LocalDateTime.now(), LocalDateTime.now()));

        gameResponseDTOs = Arrays.asList(
                new GameResponseDTO(1L, "Game 1", "Desc 1", "Dev 1", "Pub 1", "Action", 2020, 50.0,
                        LocalDateTime.now(), LocalDateTime.now()),
                new GameResponseDTO(2L, "Game 2", "Desc 2", "Dev 2", "Pub 2", "RPG", 2021, 60.0,
                        LocalDateTime.now(), LocalDateTime.now()));
    }

    @Test
    void shouldListAllGamesSuccessfully() {
        when(gameRepository.findAll()).thenReturn(games);
        when(gameMapper.toResponseDTO(games.get(0))).thenReturn(gameResponseDTOs.get(0));
        when(gameMapper.toResponseDTO(games.get(1))).thenReturn(gameResponseDTOs.get(1));

        List<GameResponseDTO> result = listGamesUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Game 1", result.get(0).getTitle());
        assertEquals("Game 2", result.get(1).getTitle());

        verify(gameRepository, times(1)).findAll();
        verify(gameMapper, times(2)).toResponseDTO(any(Game.class));
    }

    @Test
    void shouldReturnEmptyListWhenNoGames() {
        when(gameRepository.findAll()).thenReturn(List.of());

        List<GameResponseDTO> result = listGamesUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(gameRepository, times(1)).findAll();
        verify(gameMapper, never()).toResponseDTO(any());
    }
}
