package com.projuris.gamescatalog.infrastructure.persistence.mapper;

import com.projuris.gamescatalog.domain.model.Game;
import com.projuris.gamescatalog.infrastructure.persistence.entity.GameEntity;
import org.springframework.stereotype.Component;

/**
 * Mapper para conversão entre entidades de domínio e entidades de persistência
 */
@Component
public class GameEntityMapper {

    public GameEntity toEntity(Game game) {
        if (game == null) {
            return null;
        }

        GameEntity entity = new GameEntity();
        entity.setId(game.getId());
        entity.setTitle(game.getTitle());
        entity.setDescription(game.getDescription());
        entity.setDeveloper(game.getDeveloper());
        entity.setPublisher(game.getPublisher());
        entity.setGenre(game.getGenre());
        entity.setReleaseYear(game.getReleaseYear());
        entity.setPrice(game.getPrice());
        entity.setCreatedAt(game.getCreatedAt());
        entity.setUpdatedAt(game.getUpdatedAt());

        return entity;
    }

    public Game toDomain(GameEntity entity) {
        if (entity == null) {
            return null;
        }

        return Game.reconstruct(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getDeveloper(),
                entity.getPublisher(),
                entity.getGenre(),
                entity.getReleaseYear(),
                entity.getPrice(),
                entity.getCreatedAt(),
                entity.getUpdatedAt());
    }
}
