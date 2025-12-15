package com.projuris.gamescatalog.application.usecase;

import com.projuris.gamescatalog.application.dto.GameResponseDTO;
import com.projuris.gamescatalog.application.dto.PageResponseDTO;
import com.projuris.gamescatalog.application.mapper.GameMapper;
import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use Case para listar todos os games
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ListGamesUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Cacheable(value = "games")
    public List<GameResponseDTO> execute() {
        log.debug("ListGamesUseCase.execute - Listando todos os games");
        List<Game> games = gameRepository.findAll();
        log.debug("ListGamesUseCase.execute - Encontrados {} games no repositório", games.size());

        List<GameResponseDTO> result = games.stream()
                .map(gameMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.debug("ListGamesUseCase.execute - Retornando {} games", result.size());
        return result;
    }

    public PageResponseDTO<GameResponseDTO> execute(int page, int size) {
        log.debug("ListGamesUseCase.execute - Listando games paginados: page={}, size={}", page, size);

        List<Game> games = gameRepository.findAll(page, size);
        long totalElements = gameRepository.count();
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<GameResponseDTO> content = games.stream()
                .map(gameMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.debug("ListGamesUseCase.execute - Retornando página {} de {} com {} games", page, totalPages,
                content.size());

        PageResponseDTO<GameResponseDTO> response = new PageResponseDTO<>(
                content,
                page,
                size,
                totalElements,
                totalPages,
                page == 0,
                page >= totalPages - 1);

        return response;
    }
}
