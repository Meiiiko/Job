package com.buer.job.service.cache;

import com.buer.job.service.cache.inter.IRedisCacheProvider;
import com.buer.job.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.GenericTypeResolver;

/**
 * Created by jiewu on 2021/1/26
 */
public abstract class AbstractRedisCache<K, V> {
  private IRedisCacheProvider cacheProvider;
  private String prefix;
  private Class<K> keyClass;
  private Class<V> modelClass;

  public AbstractRedisCache(IRedisCacheProvider cacheProvider, String prefix) {
    this.cacheProvider = cacheProvider;
    this.prefix = prefix;
    this.init();
  }

  protected void init() {
    Class<?>[] classes = GenericTypeResolver.resolveTypeArguments(getClass(), AbstractRedisCache.class);
    this.keyClass = (Class<K>) classes[0];
    this.modelClass = (Class<V>) classes[1];
  }

  private String generateKey(K key) {
    return prefix + "_" + JsonUtils.toString(key);
  }

  public V get(K key) {
    String val = cacheProvider.get(generateKey(key));
    if (StringUtils.isBlank(val)) {
      return null;
    }
    return JsonUtils.from(val, modelClass);
  }

  public Boolean set(K key, V value, Long expireTime) {
    String generateKey = generateKey(key);
    String val = JsonUtils.toString(value);
    return cacheProvider.set(generateKey, val, expireTime);
  }


}
