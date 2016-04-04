package com.riadh.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
@EnableConfigurationProperties
public class PropertiesConfig {

    @Component
    @ConfigurationProperties(prefix = "env.cache")
    public static class CacheProperties {

        private String name;

        private Integer port;

        private String multicastIp;

        private Integer multicastPort;

        private String interfaceIp;

        private List<String> members;

        private CacheInstanceProperties defaultInstance;

        private CacheInstanceProperties screenNavInstance;

        private CacheInstanceProperties referentielInstance;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public String getMulticastIp() {
            return multicastIp;
        }

        public void setMulticastIp(String multicastIp) {
            this.multicastIp = multicastIp;
        }

        public Integer getMulticastPort() {
            return multicastPort;
        }

        public void setMulticastPort(Integer multicastPort) {
            this.multicastPort = multicastPort;
        }

        public String getInterfaceIp() {
            return interfaceIp;
        }

        public void setInterfaceIp(String interfaceIp) {
            this.interfaceIp = interfaceIp;
        }

        public List<String> getMembers() {
            return members;
        }

        public void setMembers(List<String> members) {
            this.members = members;
        }

        public CacheInstanceProperties getDefaultInstance() {
            return defaultInstance;
        }

        public void setDefaultInstance(CacheInstanceProperties defaultInstance) {
            this.defaultInstance = defaultInstance;
        }

        public CacheInstanceProperties getScreenNavInstance() {
            return screenNavInstance;
        }

        public void setScreenNavInstance(
                CacheInstanceProperties screenNavInstance) {
            this.screenNavInstance = screenNavInstance;
        }

        public CacheInstanceProperties getReferentielInstance() {
            return referentielInstance;
        }

        public void setReferentielInstance(
                CacheInstanceProperties referentielInstance) {
            this.referentielInstance = referentielInstance;
        }
    }

    /**
     * Objet représentant une instance de cache
     */
    public static class CacheInstanceProperties {

        private Integer timeToLiveSeconds;

        private Integer backupCount;

        public Integer getTimeToLiveSeconds() {
            return timeToLiveSeconds;
        }

        public void setTimeToLiveSeconds(Integer timeToLiveSeconds) {
            this.timeToLiveSeconds = timeToLiveSeconds;
        }

        public Integer getBackupCount() {
            return backupCount;
        }

        public void setBackupCount(Integer backupCount) {
            this.backupCount = backupCount;
        }
    }
}
