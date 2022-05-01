package com.spring.azure.springazurecloud.service.cache;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.spring.azure.springazurecloud.utils.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
public class CacheService {
    private final CacheManager cacheManager;

    public <K, V> boolean putInCache(String cacheName, K key, V value){
        ObjectWriter ow = new ObjectMapper().writer();
        return ofNullable(cacheName)
                .map(cacheManager::getCache)
                .map(c -> {
                    Optional<String> cacheValue = JsonUtil.toJson(value);
                    c.put(key, cacheValue.get());
                    return true;
                })
                .orElse(false);
    }

    public <K, V>Optional<V> getFromCache(String cacheName, K key){
        return ofNullable(cacheName)
                .map(cacheManager::getCache)
                .map(c -> c.get(key))
                .map(v -> (V) v.get());
    }

    public boolean clear(String cacheName) {
        return ofNullable(cacheName)
                .map(cacheManager::getCache)
                .map(c -> {
                    c.clear();
                    return true;
                })
                .orElse(false);
    }

    public <K> boolean remove(String cacheName, K key) {
        return ofNullable(cacheName)
                .map(cacheManager::getCache)
                .map(c -> {
                    c.evict(key);
                    return true;
                })
                .orElse(false);
    }


}
