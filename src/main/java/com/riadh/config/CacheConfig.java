package com.riadh.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.TcpIpConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.instance.HazelcastInstanceFactory;
import com.hazelcast.spring.cache.HazelcastCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import com.riadh.config.PropertiesConfig.CacheProperties;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@Configuration
@EnableCaching
public class CacheConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(CacheConfig.class);

    public static final String DEFAULT_CACHE = "default";

    public static final String SERVICE_CACHE = "com.riadh.*";

    @Autowired
    private CacheProperties cacheProperties;

    @Autowired
    private Environment environment;

    private static HazelcastInstance hazelcastInstance;

    /**
     * Création du cache manager Spring avec l'implémentation d'Hazelcast
     *
     * @return le cache manager configuré
     */
    @Bean
    public CacheManager cacheManager() {
        return new HazelcastCacheManager(hazelcastInstance);
    }

    /**
     * Création du cache resolver Spring avec l'implémentation d'Hazelcast
     *
     * @return le cache resolver configuré
     */
    @Bean(name = "serviceCacheResolver")
    public CacheResolver cacheResolver() {
        ServiceCacheResolver cacheResolver = new ServiceCacheResolver();
        cacheResolver.setCacheManager(cacheManager());
        return cacheResolver;
    }

    /**
     * Construit l'instance du cache Hazelcast
     *
     * @return instance initialisée du cache Hazelcast
     */
    @PostConstruct
    public HazelcastInstance hazelcastInstance() {
        shutdown();
        Config config = new Config();
        globalCacheConfig(config);
        addCachesToConfig(config);
        hazelcastInstance = HazelcastInstanceFactory.newHazelcastInstance(config);
        LOGGER.trace("Configuration de l'instance de cache Hazelcast");
        return hazelcastInstance;
    }

    /**
     * Ferme le Hazelcast et ehCache
     */
    @PreDestroy
    public void shutdown() {
        Hazelcast.shutdownAll();
    }

    private void globalCacheConfig(Config config) {
        config.setInstanceName(cacheProperties.getName());
        config.getNetworkConfig().setPort(cacheProperties.getPort());
        if (environment.acceptsProfiles("developpement")) {
            localConfig(config);
        } else {
            clusterConfig(config);
        }
    }

    private void addCachesToConfig(Config config) {
        config.getMapConfigs().put(DEFAULT_CACHE, initDefaultConfig());
        config.getMapConfigs().put(SERVICE_CACHE, initReferentielConfig());
    }

    private void localConfig(Config config) {
        System.setProperty("hazelcast.local.localAddress", "127.0.0.1");
        config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
    }

    private void clusterConfig(Config config) {
        config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
        config.getNetworkConfig().getJoin().getAwsConfig().setEnabled(false);
        TcpIpConfig tcpIpConfig = config.getNetworkConfig().getJoin().getTcpIpConfig();
        tcpIpConfig.setEnabled(true);
        cacheProperties.getMembers().forEach(tcpIpConfig::addMember);
    }

    private MapConfig initDefaultConfig() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setBackupCount(cacheProperties.getDefaultInstance().getBackupCount());
        mapConfig.setTimeToLiveSeconds(cacheProperties.getDefaultInstance().getTimeToLiveSeconds());
        mapConfig.setEvictionPolicy(MapConfig.DEFAULT_EVICTION_POLICY.LRU);
        return mapConfig;
    }

    private MapConfig initReferentielConfig() {
        MapConfig mapConfig = new MapConfig();
        mapConfig.setBackupCount(cacheProperties.getReferentielInstance().getBackupCount());
        mapConfig.setTimeToLiveSeconds(cacheProperties.getReferentielInstance().getTimeToLiveSeconds());
        return mapConfig;
    }

}
