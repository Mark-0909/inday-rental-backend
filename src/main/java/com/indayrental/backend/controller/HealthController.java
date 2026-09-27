package com.indayrental.backend.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class HealthController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${supabase.url}")
    private String supabaseUrl;

    @Value("${supabase.key}")
    private String supabaseKey;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("backend", "OK");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.set("apikey", supabaseKey);
            HttpEntity<String> entity = new HttpEntity<>(headers);

            restTemplate.exchange(
                supabaseUrl + "/auth/v1/health",
                HttpMethod.GET,
                entity,
                String.class
            );
            response.put("supabase", "OK");
        } catch (Exception e) {
            response.put("supabase", "WARN: Unable to reach database");
        }

        return ResponseEntity.ok(response);
    }
}