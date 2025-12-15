package com.projuris.gamescatalog.infrastructure.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Configuração do Spring Cache usando Caffeine
 * Cache em memória para melhorar performance de consultas frequentes
 */
@Configuration
@EnableCaching
public class CacheConfig {

    public static final String GAMES_CACHE = "games";
    public static final String GAME_BY_ID_CACHE = "gameById";
    public static final String GAMES_BY_GENRE_CACHE = "gamesByGenre";

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(
                GAMES_CACHE,
                GAME_BY_ID_CACHE,
                GAMES_BY_GENRE_CACHE);

        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .expireAfterAccess(5, TimeUnit.MINUTES)
                .recordStats());

        return cacheManager;
    }
}
