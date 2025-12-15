package com.projuris.gamescatalog.application.usecase;

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
 * Use Case para deletar um game
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DeleteGameUseCase {

    private final GameRepository gameRepository;
    private final DomainEventPublisher eventPublisher;

    @Transactional
    @CacheEvict(value = { "gameById", "games", "gamesByGenre" }, allEntries = true)
    public void execute(Long id) {
        log.debug("DeleteGameUseCase.execute - Deletando game com ID: {}", id);

        Game game = gameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("DeleteGameUseCase.execute - Game não encontrado com ID: {}", id);
                    return new NoSuchElementException("Game não encontrado com ID: " + id);
                });

        log.debug("DeleteGameUseCase.execute - Game encontrado: title={}, marcando para deleção", game.getTitle());
        game.delete();
        eventPublisher.publishDomainEvents(game);

        gameRepository.deleteById(id);
        log.debug("DeleteGameUseCase.execute - Game deletado com sucesso: id={}", id);
    }
}
