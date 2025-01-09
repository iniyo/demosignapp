package com.example.demosignapp;

import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisClusterTester {

    @Autowired
    private StatefulRedisClusterConnection<String, String> connection;

    @Test
    void testRedisClusterConnection() {
        try {
            String response = connection.sync().ping();
            System.out.println("Redis cluster connection successful: " + response);
            assertThat(response).isEqualTo("PONG");
        } catch (Exception e) {
            System.err.println("Redis cluster connection failed: " + e.getMessage());
            throw e;
        }
    }
}
