package com.projuris.gamescatalog.application.usecase;

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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteGameUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private DeleteGameUseCase deleteGameUseCase;

    private Game game;

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
    }

    @Test
    void shouldDeleteGameSuccessfully() {
        Long gameId = 1L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.of(game));
        doNothing().when(gameRepository).deleteById(gameId);
        doNothing().when(eventPublisher).publishDomainEvents(any(Game.class));

        assertDoesNotThrow(() -> deleteGameUseCase.execute(gameId));

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, times(1)).deleteById(gameId);
        verify(eventPublisher, times(1)).publishDomainEvents(any(Game.class));
    }

    @Test
    void shouldThrowExceptionWhenGameNotFound() {
        Long gameId = 999L;
        when(gameRepository.findById(gameId)).thenReturn(Optional.empty());

        assertThrows(java.util.NoSuchElementException.class, () -> {
            deleteGameUseCase.execute(gameId);
        });

        verify(gameRepository, times(1)).findById(gameId);
        verify(gameRepository, never()).deleteById(any());
    }
}
