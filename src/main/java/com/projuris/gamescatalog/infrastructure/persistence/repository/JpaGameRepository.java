package com.projuris.gamescatalog.infrastructure.persistence.repository;

import com.projuris.gamescatalog.infrastructure.persistence.entity.GameEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaGameRepository extends JpaRepository<GameEntity, Long> {

    List<GameEntity> findByGenre(String genre);

    Page<GameEntity> findByGenre(String genre, Pageable pageable);

    @Query("SELECT COUNT(g) FROM GameEntity g WHERE g.genre = :genre")
    long countByGenre(@Param("genre") String genre);

    List<GameEntity> findByDeveloper(String developer);
}
