/*
 * Copyright (c) 2019-2023  YunLong Chen
 * Project Qing is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 *
 */

package cn.chenyunlong.qing.infrastructure.cache;

import org.springframework.lang.NonNull;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Cache store interface.
 *
 * @param <K> cache key type
 * @param <V> cache value type
 * @author Stan
 * *
 */
public interface CacheStore<K, V> {

    /**
     * Gets by cache key.
     *
     * @param key must not be null
     * @return cache value
     */
    @NonNull
    Optional<V> get(@NonNull K key);

    /**
     * Puts a cache which will be expired.
     *
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit
     */
    void put(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);

    /**
     * Puts a cache which will be expired if the key is absent.
     *
     * @param key      cache key must not be null
     * @param value    cache value must not be null
     * @param timeout  the key expiration must not be less than 1
     * @param timeUnit timeout unit must not be null
     * @return true if the key is absent and the value is set, false if the key is present before, or null if any other reason
     */
    Boolean putIfAbsent(@NonNull K key, @NonNull V value, long timeout, @NonNull TimeUnit timeUnit);

    /**
     * Puts a non-expired cache.
     *
     * @param key   cache key must not be null
     * @param value cache value must not be null
     */
    void put(@NonNull K key, @NonNull V value);

    /**
     * Delete a key.
     *
     * @param key cache key must not be null
     */
    void delete(@NonNull K key);

}
