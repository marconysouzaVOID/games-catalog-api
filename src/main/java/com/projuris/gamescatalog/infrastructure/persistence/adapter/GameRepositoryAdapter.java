package com.projuris.gamescatalog.infrastructure.persistence.adapter;

import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.domain.repository.GameRepository;
import com.projuris.gamescatalog.infrastructure.persistence.entity.GameEntity;
import com.projuris.gamescatalog.infrastructure.persistence.mapper.GameEntityMapper;
import com.projuris.gamescatalog.infrastructure.persistence.repository.JpaGameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Adapter que implementa o port GameRepository
 * Conecta a camada de domínio com a infraestrutura JPA
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GameRepositoryAdapter implements GameRepository {

    private final JpaGameRepository jpaGameRepository;
    private final GameEntityMapper gameEntityMapper;

    @Override
    public Game save(Game game) {
        log.trace("GameRepositoryAdapter.save - Salvando game: title={}, id={}", game.getTitle(), game.getId());
        GameEntity entity = gameEntityMapper.toEntity(game);
        GameEntity savedEntity = jpaGameRepository.save(entity);
        log.trace("GameRepositoryAdapter.save - Game salvo: id={}", savedEntity.getId());
        return gameEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Game> findById(Long id) {
        log.trace("GameRepositoryAdapter.findById - Buscando game: id={}", id);
        Optional<Game> result = jpaGameRepository.findById(id)
                .map(gameEntityMapper::toDomain);
        log.trace("GameRepositoryAdapter.findById - Game {} encontrado: {}", id, result.isPresent());
        return result;
    }

    @Override
    public List<Game> findAll() {
        log.trace("GameRepositoryAdapter.findAll - Buscando todos os games");
        List<Game> result = jpaGameRepository.findAll().stream()
                .map(gameEntityMapper::toDomain)
                .collect(Collectors.toList());
        log.trace("GameRepositoryAdapter.findAll - Encontrados {} games", result.size());
        return result;
    }

    @Override
    public List<Game> findAll(int page, int size) {
        log.trace("GameRepositoryAdapter.findAll - Buscando games paginados: page={}, size={}", page, size);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        List<Game> result = jpaGameRepository.findAll(pageable).getContent().stream()
                .map(gameEntityMapper::toDomain)
                .collect(Collectors.toList());
        log.trace("GameRepositoryAdapter.findAll - Encontrados {} games na página {}", result.size(), page);
        return result;
    }

    @Override
    public List<Game> findByGenre(String genre) {
        log.trace("GameRepositoryAdapter.findByGenre - Buscando games por gênero: {}", genre);
        List<Game> result = jpaGameRepository.findByGenre(genre).stream()
                .map(gameEntityMapper::toDomain)
                .collect(Collectors.toList());
        log.trace("GameRepositoryAdapter.findByGenre - Encontrados {} games para gênero {}", result.size(), genre);
        return result;
    }

    @Override
    public List<Game> findByGenre(String genre, int page, int size) {
        log.trace("GameRepositoryAdapter.findByGenre - Buscando games por gênero paginados: genre={}, page={}, size={}",
                genre, page, size);
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        List<Game> result = jpaGameRepository.findByGenre(genre, pageable).getContent().stream()
                .map(gameEntityMapper::toDomain)
                .collect(Collectors.toList());
        log.trace("GameRepositoryAdapter.findByGenre - Encontrados {} games para gênero {} na página {}", result.size(),
                genre, page);
        return result;
    }

    @Override
    public long count() {
        log.trace("GameRepositoryAdapter.count - Contando total de games");
        long count = jpaGameRepository.count();
        log.trace("GameRepositoryAdapter.count - Total de games: {}", count);
        return count;
    }

    @Override
    public long countByGenre(String genre) {
        log.trace("GameRepositoryAdapter.countByGenre - Contando games por gênero: {}", genre);
        long count = jpaGameRepository.countByGenre(genre);
        log.trace("GameRepositoryAdapter.countByGenre - Total de games para gênero {}: {}", genre, count);
        return count;
    }

    @Override
    public List<Game> findByDeveloper(String developer) {
        log.trace("GameRepositoryAdapter.findByDeveloper - Buscando games por desenvolvedora: {}", developer);
        List<Game> result = jpaGameRepository.findByDeveloper(developer).stream()
                .map(gameEntityMapper::toDomain)
                .collect(Collectors.toList());
        log.trace("GameRepositoryAdapter.findByDeveloper - Encontrados {} games para desenvolvedora {}", result.size(),
                developer);
        return result;
    }

    @Override
    public void deleteById(Long id) {
        log.trace("GameRepositoryAdapter.deleteById - Deletando game: id={}", id);
        jpaGameRepository.deleteById(id);
        log.trace("GameRepositoryAdapter.deleteById - Game deletado: id={}", id);
    }

    @Override
    public boolean existsById(Long id) {
        log.trace("GameRepositoryAdapter.existsById - Verificando existência: id={}", id);
        boolean exists = jpaGameRepository.existsById(id);
        log.trace("GameRepositoryAdapter.existsById - Game {} existe: {}", id, exists);
        return exists;
    }
}
