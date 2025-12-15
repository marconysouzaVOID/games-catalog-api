package com.projuris.gamescatalog.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "GAMES", indexes = {
        @Index(name = "idx_games_genre", columnList = "genre"),
        @Index(name = "idx_games_developer", columnList = "developer"),
        @Index(name = "idx_games_title", columnList = "title"),
        @Index(name = "idx_games_release_year", columnList = "releaseYear"),
        @Index(name = "idx_games_genre_release_year", columnList = "genre,releaseYear")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, length = 100)
    private String developer;

    @Column(length = 100)
    private String publisher;

    @Column(nullable = false, length = 50)
    private String genre;

    @Column(nullable = false)
    private Integer releaseYear;

    @Column(nullable = false)
    private Double price;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
