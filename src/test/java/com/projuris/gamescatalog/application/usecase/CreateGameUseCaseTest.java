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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateGameUseCaseTest {

    @Mock
    private GameRepository gameRepository;

    @Mock
    private GameMapper gameMapper;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private CreateGameUseCase createGameUseCase;

    private GameRequestDTO gameRequestDTO;
    private GameResponseDTO gameResponseDTO;

    @BeforeEach
    void setUp() {
        gameRequestDTO = new GameRequestDTO(
                "The Witcher 3",
                "RPG de mundo aberto",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                99.90);

        gameResponseDTO = new GameResponseDTO(
                1L,
                "The Witcher 3",
                "RPG de mundo aberto",
                "CD Projekt RED",
                "CD Projekt",
                "RPG",
                2015,
                99.90,
                null,
                null);
    }

    @Test
    void shouldCreateGameSuccessfully() {
        when(gameRepository.save(any(Game.class))).thenAnswer(invocation -> {
            Game savedGame = invocation.getArgument(0);

            Game gameWithId = Game.reconstruct(
                    1L,
                    savedGame.getTitle(),
                    savedGame.getDescription(),
                    savedGame.getDeveloper(),
                    savedGame.getPublisher(),
                    savedGame.getGenre(),
                    savedGame.getReleaseYear(),
                    savedGame.getPrice(),
                    savedGame.getCreatedAt(),
                    savedGame.getUpdatedAt());
            return gameWithId;
        });
        when(gameMapper.toResponseDTO(any(Game.class))).thenReturn(gameResponseDTO);
        doNothing().when(eventPublisher).publishDomainEvents(any(Game.class));

        GameResponseDTO result = createGameUseCase.execute(gameRequestDTO);

        assertNotNull(result);
        assertEquals("The Witcher 3", result.getTitle());
        assertEquals("CD Projekt RED", result.getDeveloper());
        assertEquals("RPG", result.getGenre());

        verify(gameRepository, times(1)).save(any(Game.class));
        verify(gameMapper, times(1)).toResponseDTO(any(Game.class));
        verify(eventPublisher, times(1)).publishDomainEvents(any(Game.class));
    }
}
