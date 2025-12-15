package com.projuris.gamescatalog.domain.model;

import com.projuris.gamescatalog.domain.events.GameCreatedEvent;
import com.projuris.gamescatalog.domain.events.GameDeletedEvent;
import com.projuris.gamescatalog.domain.events.GameUpdatedEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class Game {

    private Long id;
    private String title;
    private String description;
    private String developer;
    private String publisher;
    private String genre;
    private Integer releaseYear;
    private Double price;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Lista de eventos de domínio
    private final List<Object> domainEvents = new ArrayList<>();

    private Game(Long id, String title, String description, String developer,
            String publisher, String genre, Integer releaseYear, Double price) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.publisher = publisher;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Game create(String title, String description, String developer,
            String publisher, String genre, Integer releaseYear, Double price) {
        Game game = new Game(null, title, description, developer, publisher, genre, releaseYear, price);
        // Evento será criado após salvar quando o ID estiver disponível
        return game;
    }

    /**
     * Marca o game como criado (chamado após persistir)
     */
    public void markAsCreated() {
        this.addDomainEvent(new GameCreatedEvent(this));
    }

    /**
     * Reconstrói um Game a partir de dados persistidos
     * Usado pelo adapter de repositório para reconstruir a entidade de domínio
     */
    public static Game reconstruct(Long id, String title, String description, String developer,
            String publisher, String genre, Integer releaseYear, Double price,
            LocalDateTime createdAt, LocalDateTime updatedAt) {
        Game game = new Game();
        game.id = id;
        game.title = title;
        game.description = description;
        game.developer = developer;
        game.publisher = publisher;
        game.genre = genre;
        game.releaseYear = releaseYear;
        game.price = price;
        game.createdAt = createdAt;
        game.updatedAt = updatedAt;
        return game;
    }

    public void update(String title, String description, String developer,
            String publisher, String genre, Integer releaseYear, Double price) {
        this.title = title;
        this.description = description;
        this.developer = developer;
        this.publisher = publisher;
        this.genre = genre;
        this.releaseYear = releaseYear;
        this.price = price;
        this.updatedAt = LocalDateTime.now();
        this.addDomainEvent(new GameUpdatedEvent(this));
    }

    public void delete() {
        this.addDomainEvent(new GameDeletedEvent(this));
    }

    private void addDomainEvent(Object event) {
        this.domainEvents.add(event);
    }

    public List<Object> getDomainEvents() {
        return new ArrayList<>(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }
}
