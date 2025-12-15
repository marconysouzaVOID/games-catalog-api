package com.projuris.gamescatalog.infrastructure.events.handler;

import com.projuris.gamescatalog.domain.events.GameDeletedEvent;
import com.projuris.gamescatalog.infrastructure.config.CacheConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * Handler para eventos de deleção de games
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class GameDeletedEventHandler {

    private final CacheManager cacheManager;

    @EventListener
    @Async
    public void handleGameDeleted(GameDeletedEvent event) {
        log.info("=== EVENTO PROCESSADO: Game Deletado ===");
        log.info("ID do Game: {}", event.getGameId());
        log.info("Título: {}", event.getTitle());
        log.info("Data/Hora: {}", event.getOccurredAt());
        log.info("========================================");

        // Invalidando todos os caches relacionados ao game deletado
        log.debug("GameDeletedEventHandler - Invalidando caches: gameById, games, gamesByGenre");
        cacheManager.getCache(CacheConfig.GAME_BY_ID_CACHE).evict(event.getGameId());
        cacheManager.getCache(CacheConfig.GAMES_CACHE).clear();
        cacheManager.getCache(CacheConfig.GAMES_BY_GENRE_CACHE).clear();
        log.debug("GameDeletedEventHandler - Caches invalidados com sucesso");

        // Aqui pode ser implementada lógica adicional como:
        // - Limpar recursos relacionados (ex: Redis)
        // - Notificar sistemas externos (ex: EmailService ou Slack)
        // - Registrar auditoria (ex: AuditService ou Log)
    }
}
