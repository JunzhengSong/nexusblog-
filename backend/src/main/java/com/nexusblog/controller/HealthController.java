package com.nexusblog.controller;

import com.nexusblog.common.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class HealthController {

    private final DataSource dataSource;

    @GetMapping("/health")
    public ApiResult<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        health.put("status", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("service", "NexusBlog API");

        try (Connection connection = dataSource.getConnection()) {
            Map<String, Object> dbInfo = new HashMap<>();
            dbInfo.put("status", "UP");
            dbInfo.put("database", connection.getMetaData().getDatabaseProductName());
            dbInfo.put("url", connection.getMetaData().getURL());
            health.put("database", dbInfo);
        } catch (Exception e) {
            Map<String, Object> dbInfo = new HashMap<>();
            dbInfo.put("status", "DOWN");
            dbInfo.put("error", e.getMessage());
            health.put("database", dbInfo);
            health.put("status", "DEGRADED");
        }

        return ApiResult.ok(health);
    }
}
