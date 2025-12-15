package com.projuris.gamescatalog.infrastructure.events;

import com.projuris.gamescatalog.domain.events.GameCreatedEvent;
import com.projuris.gamescatalog.domain.events.GameDeletedEvent;
import com.projuris.gamescatalog.domain.events.GameUpdatedEvent;
import com.projuris.gamescatalog.domain.model.Game;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

/**
 * Publicador de eventos de domínio
 * Converte eventos de domínio em eventos do Spring e os publica
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DomainEventPublisher {

    private final ApplicationEventPublisher eventPublisher;

    public void publishDomainEvents(Game game) {
        game.getDomainEvents().forEach(event -> {
            if (event instanceof GameCreatedEvent) {
                GameCreatedEvent createdEvent = (GameCreatedEvent) event;
                log.info("Publicando evento: Game criado - ID: {}, Título: {}",
                        createdEvent.getGameId(), createdEvent.getTitle());
                eventPublisher.publishEvent(createdEvent);
            } else if (event instanceof GameUpdatedEvent) {
                GameUpdatedEvent updatedEvent = (GameUpdatedEvent) event;
                log.info("Publicando evento: Game atualizado - ID: {}, Título: {}",
                        updatedEvent.getGameId(), updatedEvent.getTitle());
                eventPublisher.publishEvent(updatedEvent);
            } else if (event instanceof GameDeletedEvent) {
                GameDeletedEvent deletedEvent = (GameDeletedEvent) event;
                log.info("Publicando evento: Game deletado - ID: {}, Título: {}",
                        deletedEvent.getGameId(), deletedEvent.getTitle());
                eventPublisher.publishEvent(deletedEvent);
            }
        });

        game.clearDomainEvents();
    }
}
