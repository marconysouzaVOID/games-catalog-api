package com.projuris.gamescatalog.domain.repository;

import com.projuris.gamescatalog.domain.model.Game;

import java.util.List;
import java.util.Optional;

/**
 * Port (Interface) para o repositório de Games
 * Define o contrato sem depender de implementações específicas
 */
public interface GameRepository {

    Game save(Game game);

    Optional<Game> findById(Long id);

    List<Game> findAll();

    List<Game> findAll(int page, int size);

    List<Game> findByGenre(String genre);

    List<Game> findByGenre(String genre, int page, int size);

    long count();

    long countByGenre(String genre);

    List<Game> findByDeveloper(String developer);

    void deleteById(Long id);

    boolean existsById(Long id);
}
