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
 * Use Case para listar games por gênero
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ListGamesByGenreUseCase {

    private final GameRepository gameRepository;
    private final GameMapper gameMapper;

    @Cacheable(value = "gamesByGenre", key = "#genre")
    public List<GameResponseDTO> execute(String genre) {
        log.debug("ListGamesByGenreUseCase.execute - Listando games por gênero: {}", genre);
        List<Game> games = gameRepository.findByGenre(genre);
        log.debug("ListGamesByGenreUseCase.execute - Encontrados {} games para gênero {}", games.size(), genre);

        List<GameResponseDTO> result = games.stream()
                .map(gameMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.debug("ListGamesByGenreUseCase.execute - Retornando {} games", result.size());
        return result;
    }

    public PageResponseDTO<GameResponseDTO> execute(String genre, int page, int size) {
        log.debug("ListGamesByGenreUseCase.execute - Listando games por gênero paginados: genre={}, page={}, size={}",
                genre, page, size);

        List<Game> games = gameRepository.findByGenre(genre, page, size);
        long totalElements = gameRepository.countByGenre(genre);
        int totalPages = (int) Math.ceil((double) totalElements / size);

        List<GameResponseDTO> content = games.stream()
                .map(gameMapper::toResponseDTO)
                .collect(Collectors.toList());

        log.debug("ListGamesByGenreUseCase.execute - Retornando página {} de {} com {} games para gênero {}", page,
                totalPages, content.size(), genre);

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
