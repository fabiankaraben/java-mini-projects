package com.example.crawler.config;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastConfig {

    @Bean
    public Config crawlerHazelcastConfig() {
        Config config = new Config();
        config.setInstanceName("crawler-hazelcast-instance");
        
        JoinConfig joinConfig = config.getNetworkConfig().getJoin();
        joinConfig.getMulticastConfig().setEnabled(false);
        
        String members = System.getenv("HAZELCAST_MEMBERS");
        if (members != null && !members.isEmpty()) {
            for (String member : members.split(",")) {
                joinConfig.getTcpIpConfig().addMember(member.trim());
            }
            joinConfig.getTcpIpConfig().setEnabled(true);
        } else {
            // Default to localhost for local testing
            joinConfig.getTcpIpConfig().setEnabled(true).addMember("127.0.0.1");
        }

        return config;
    }

    @Bean
    public HazelcastInstance hazelcastInstance(Config crawlerHazelcastConfig) {
        return Hazelcast.getOrCreateHazelcastInstance(crawlerHazelcastConfig);
    }
}
