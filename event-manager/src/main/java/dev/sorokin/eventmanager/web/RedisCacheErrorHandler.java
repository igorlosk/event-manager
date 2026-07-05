package dev.sorokin.eventmanager.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

public class RedisCacheErrorHandler implements CacheErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheErrorHandler.class);

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        // Логируем, но НЕ выбрасываем исключение.
        // Тогда Spring выполнит целевой метод (обращение к БД).
        log.warn("Cache get error for cache={}, key={}. Falling back to DB. Exception: {}",
                cache.getName(), key, exception.getMessage(), exception);
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("Cache put error for cache={}, key={}. Cache update skipped. Exception: {}",
                cache.getName(), key, exception.getMessage(), exception);
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Cache evict error for cache={}, key={}. Exception: {}",
                cache.getName(), key, exception.getMessage(), exception);
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {

    }
}

