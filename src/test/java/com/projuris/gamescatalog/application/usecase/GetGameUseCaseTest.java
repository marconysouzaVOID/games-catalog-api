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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetGameUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @InjectMocks
    private GetGameUseCase getGameUseCase;

    private Game game;
    private GameResponseDTO gameResponseDTO;

    @BeforeEach
    void setUp() {
        game = Game.reconstruct(
                1L,
                "The Witcher 3",
                "RPG de mundo aberto",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                99.90,
                LocalDateTime.now(),
                LocalDateTime.now());

        gameResponseDTO = new GameResponseDTO(
                1L,
                "The Witcher 3",
                "RPG de mundo aberto",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                99.90,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    void shouldGetGameByIdSuccessfully() {
        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        when(gameMapper.toResponseDTO(game)).thenReturn(gameResponseDTO);

        GameResponseDTO result = getGameUseCase.execute(gameId);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("The Witcher 3", result.getTitle());

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameMapper, times(1)).toResponseDTO(game);
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        Long gameId = 999L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            getGameUseCase.execute(gameId);
        });

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameMapper, never()).toResponseDTO(any());
    }
}
