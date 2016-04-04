package com.riadh.config;

import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.AbstractCacheResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServiceCacheResolver extends AbstractCacheResolver {

    @Override
    protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
        List<String> caches = new ArrayList<>(1);
        caches.add(context.getTarget().getClass().getName());
        return caches;
    }
}
