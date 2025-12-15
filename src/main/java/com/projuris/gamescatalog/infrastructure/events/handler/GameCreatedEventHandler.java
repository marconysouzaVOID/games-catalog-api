package com.projuris.gamescatalog.infrastructure.events.handler;

import com.projuris.gamescatalog.domain.events.GameCreatedEvent;
import com.projuris.gamescatalog.infrastructure.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Handler para eventos de criação de games
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GameCreatedEventHandler {

    private final CacheManager cacheManager;

    @EventListener
    @Async
    public void handleGameCreated(GameCreatedEvent event) {
        log.info("=== EVENTO PROCESSADO: Game Criado ===");
        log.info("ID do Game: {}", event.getGameId());
        log.info("Título: {}", event.getTitle());
        log.info("Desenvolvedora: {}", event.getDeveloper());
        log.info("Data/Hora: {}", event.getOccurredAt());
        log.info("=====================================");

        // Invalidando cache relacionado ao novo game
        log.debug("GameCreatedEventHandler - Invalidando caches: games, gamesByGenre");
        cacheManager.getCache(CacheConfig.GAMES_CACHE).clear();
        cacheManager.getCache(CacheConfig.GAMES_BY_GENRE_CACHE).clear();
        log.debug("GameCreatedEventHandler - Caches invalidados com sucesso");

        // Aqui pode ser implementada lógica adicional como:
        // - Enviar notificação por email (ex: EmailService ou Slack)
        // - Registrar em sistema de analytics (ex: Google Analytics)
        // - Enviar para fila de mensageria (ex: Kafka ou RabbitMQ)
    }
}
