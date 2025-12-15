package com.projuris.gamescatalog.application.usecase;

import com.projuris.gamescatalog.application.dto.GameRequestDTO;
import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.application.mapper.GameMapper;
import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import com.projuris.gamescatalog.infrastructure.events.DomainEventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateGameUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private UpdateGameUseCase updateGameUseCase;

    private Game game;
    private GameRequestDTO gameRequestDTO;
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

        gameRequestDTO = new GameRequestDTO(
                "The Witcher 3: Wild Hunt",
                "RPG de mundo aberto - Edição Completa",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                129.90);

        gameResponseDTO = new GameResponseDTO(
                1L,
                "The Witcher 3: Wild Hunt",
                "RPG de mundo aberto - Edição Completa",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                129.90,
                LocalDateTime.now(),
                LocalDateTime.now());
    }

    @Test
    void shouldUpdateGameSuccessfully() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(gameRepository.save(any(Game.class))).thenReturn(game);
        when(gameMapper.toResponseDTO(any(Game.class))).thenReturn(gameResponseDTO);
        doNothing().when(eventPublisher).publishDomainEvents(any(Game.class));

        GameResponseDTO result = updateGameUseCase.execute(1L, gameRequestDTO);

        assertNotNull(result);
        assertEquals("The Witcher 3: Wild Hunt", result.getTitle());
        assertEquals(129.90, result.getPrice());

        verify(gameRepository, times(1)).findById(1L);
        verify(gameRepository, times(1)).save(any(Game.class));
        verify(gameMapper, times(1)).toResponseDTO(any(Game.class));
        verify(eventPublisher, times(1)).publishDomainEvents(any(Game.class));
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        when(gameRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            updateGameUseCase.execute(999L, gameRequestDTO);
        });

        verify(gameRepository, times(1)).findById(999L);
        verify(gameRepository, never()).save(any());
        verify(gameMapper, never()).toResponseDTO(any());
    }
}
