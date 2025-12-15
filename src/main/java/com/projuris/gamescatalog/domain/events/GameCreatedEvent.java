package com.projuris.gamescatalog.domain.events;

import com.projuris.gamescatalog.domain.model.Game;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class GameCreatedEvent implements Serializable {
    private static final long serialVersionUID = -487239847239847239L;

    private final Long gameId;
    private final String title;
    private final String developer;
    private final LocalDateTime occurredAt;

    public GameCreatedEvent(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
        this.developer = game.getDeveloper();
        this.occurredAt = LocalDateTime.now();
    }
}
