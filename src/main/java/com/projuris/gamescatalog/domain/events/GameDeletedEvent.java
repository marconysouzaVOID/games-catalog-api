package com.projuris.gamescatalog.domain.events;

import com.projuris.gamescatalog.domain.model.Game;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
public class GameDeletedEvent implements Serializable {
    private static final long serialVersionUID = 873492384723984723L;

    private final Long gameId;
    private final String title;
    private final LocalDateTime occurredAt;

    public GameDeletedEvent(Game game) {
        this.gameId = game.getId();
        this.title = game.getTitle();
        this.occurredAt = LocalDateTime.now();
    }
}
