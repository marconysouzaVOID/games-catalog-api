package com.projuris.gamescatalog.application.usecase;

import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.application.mapper.GameMapper;
import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

/**
 * Use Case para buscar um game por ID
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GetGameUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Cacheable(value = "gameById", key = "#id")
    public GameResponseDTO execute(Long id) {
        log.debug("GetGameUseCase.execute - Buscando game com ID: {}", id);
        Game game = gameRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("GetGameUseCase.execute - Game não encontrado com ID: {}", id);
                    return new NoSuchElementException("Game não encontrado com ID: " + id);
                });

        log.debug("GetGameUseCase.execute - Game encontrado: id={}, title={}", id, game.getTitle());
        return gameMapper.toResponseDTO(game);
    }
}
