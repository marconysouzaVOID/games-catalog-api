package com.projuris.gamescatalog.domain.events;

import com.projuris.gamescatalog.domain.model.Game;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class GameUpdatedEvent implements Serializable {
    private static final long serialVersionUID = -192837465564738291L;

    private final Long gameId;
    private final String title;
    private final LocalDateTime occurredAt;

    public GameUpdatedEvent(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
        this.occurredAt = LocalDateTime.now();
    }
}
