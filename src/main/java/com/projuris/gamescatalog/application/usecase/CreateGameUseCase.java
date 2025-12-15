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

/**
 * Use Case para criação de um novo game
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CreateGameUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    @CacheEvict(value = { "games", "gamesByGenre" }, allEntries = true)
    public GameResponseDTO execute(GameRequestDTO request) {
        log.info("CreateGameUseCase.execute - Iniciando criação de game: title={}", request.getTitle());

        Game game = Game.create(
                request.getTitle(),
                request.getDescription(),
                request.getDeveloper(),
                request.getPublisher(),
                request.getGenre(),
                request.getReleaseYear(),
                request.getPrice());

        log.debug("CreateGameUseCase.execute - Game criado no domínio, salvando no repositório");
        Game savedGame = gameRepository.save(game);

        log.debug("CreateGameUseCase.execute - Game salvo com ID: {}", savedGame.getId());
        savedGame.markAsCreated();
        eventPublisher.publishDomainEvents(savedGame);

        GameResponseDTO response = gameMapper.toResponseDTO(savedGame);
        log.info("CreateGameUseCase.execute - Game criado com sucesso: id={}", response.getId());
        return response;
    }
}
