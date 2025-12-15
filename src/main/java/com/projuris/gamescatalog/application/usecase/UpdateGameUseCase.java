package com.projuris.gamescatalog.application.usecase;

import com.projuris.gamescatalog.application.dto.GameRequestDTO;
import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.application.mapper.GameMapper;
import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import com.projuris.gamescatalog.infrastructure.events.DomainEventPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;

/**
 * Use Case para atualizar um game existente
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateGameUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    @CacheEvict(value = { "gameById", "games", "gamesByGenre" }, allEntries = true)
    public GameResponseDTO execute(Long id, GameRequestDTO request) {
        log.debug("UpdateGameUseCase.execute - Atualizando game com ID: {}, title={}", id, request.getTitle());

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("UpdateGameUseCase.execute - Game não encontrado com ID: {}", id);
                    return new NoSuchElementException("Game não encontrado com ID: " + id);
                });

        log.debug("UpdateGameUseCase.execute - Game encontrado, aplicando atualizações");
        game.update(
                request.getTitle(),
                request.getDescription(),
                request.getDeveloper(),
                request.getPublisher(),
                request.getGenre(),
                request.getReleaseYear(),
                request.getPrice());

        Game updatedGame = gameRepository.save(game);
        log.debug("UpdateGameUseCase.execute - Game atualizado e salvo: id={}", updatedGame.getId());
        eventPublisher.publishDomainEvents(updatedGame);

        return gameMapper.toResponseDTO(updatedGame);
    }
}
