package com.indayrental.backend.service;

import com.indayrental.backend.exception.SupabaseStorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SupabaseStorageService {
    private static final String OBJECT_PUBLIC_MARKER = "/storage/v1/object/public/";
    private static final String OBJECT_SIGN_MARKER = "/storage/v1/object/sign/";
    private static final String OBJECT_AUTH_MARKER = "/storage/v1/object/authenticated/";
    private static final String OBJECT_MARKER = "/storage/v1/object/";

    private final HttpClient httpClient;
    private final String supabaseUrl;
    private final String supabaseSecretKey;

    public SupabaseStorageService(
            @Value("${SUPABASE_URL:}") String supabaseUrl,
            @Value("${SUPABASE_SECRET_KEY:}") String supabaseSecretKey
    ) {
        this.httpClient = HttpClient.newHttpClient();
        this.supabaseUrl = supabaseUrl;
        this.supabaseSecretKey = supabaseSecretKey;
    }

    public void deleteImages(List<String> imageUrls) {
        if (imageUrls == null || imageUrls.isEmpty()) {
            return;
        }

        if (!isSupabaseConfigured()) {
            return;
        }

        Map<String, List<String>> bucketToPaths = new HashMap<>();
        for (String imageUrl : imageUrls) {
            if (imageUrl == null || imageUrl.isBlank()) {
                continue;
            }

            if (!isSupabaseObjectUrl(imageUrl)) {
                continue;
            }

            BucketPath bucketPath = extractBucketAndPath(imageUrl);
            bucketToPaths.computeIfAbsent(bucketPath.bucket(), key -> new ArrayList<>()).add(bucketPath.path());
        }

        if (bucketToPaths.isEmpty()) {
            return;
        }

        for (Map.Entry<String, List<String>> entry : bucketToPaths.entrySet()) {
            deleteObjects(entry.getKey(), entry.getValue());
        }
    }

    private boolean isSupabaseConfigured() {
        return supabaseUrl != null && !supabaseUrl.isBlank() && supabaseSecretKey != null && !supabaseSecretKey.isBlank();
    }

    private boolean isSupabaseObjectUrl(String imageUrl) {
        return imageUrl.contains(OBJECT_PUBLIC_MARKER)
                || imageUrl.contains(OBJECT_SIGN_MARKER)
                || imageUrl.contains(OBJECT_AUTH_MARKER)
                || imageUrl.contains(OBJECT_MARKER);
    }

    private BucketPath extractBucketAndPath(String imageUrl) {
        final URI imageUri;
        try {
            imageUri = new URI(imageUrl);
        } catch (URISyntaxException ex) {
            throw new SupabaseStorageException("Invalid image URL: " + imageUrl, ex);
        }

        String path = imageUri.getPath();
        if (path == null || path.isBlank()) {
            throw new SupabaseStorageException("Image URL path is empty: " + imageUrl);
        }

        String objectPart = extractObjectPart(path);
        int slashIndex = objectPart.indexOf('/');
        if (slashIndex <= 0 || slashIndex == objectPart.length() - 1) {
            throw new SupabaseStorageException("Unable to extract bucket/object path from image URL: " + imageUrl);
        }

        String bucket = objectPart.substring(0, slashIndex);
        String objectPath = objectPart.substring(slashIndex + 1);
        return new BucketPath(bucket, objectPath);
    }

    private String extractObjectPart(String path) {
        int markerIndex = path.indexOf(OBJECT_PUBLIC_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + OBJECT_PUBLIC_MARKER.length());
        }

        markerIndex = path.indexOf(OBJECT_SIGN_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + OBJECT_SIGN_MARKER.length());
        }

        markerIndex = path.indexOf(OBJECT_AUTH_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + OBJECT_AUTH_MARKER.length());
        }

        markerIndex = path.indexOf(OBJECT_MARKER);
        if (markerIndex >= 0) {
            return path.substring(markerIndex + OBJECT_MARKER.length());
        }

        throw new SupabaseStorageException("Unsupported Supabase image URL format: " + path);
    }

    private void deleteObjects(String bucket, List<String> objectPaths) {
        String payload = buildDeletePayload(objectPaths);

        String endpoint = resolveStorageBaseUrl() + "/object/" + bucket;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .header("apikey", supabaseSecretKey)
                .header("Authorization", "Bearer " + supabaseSecretKey)
                .header("Content-Type", "application/json")
                .method("DELETE", HttpRequest.BodyPublishers.ofString(payload))
                .build();

        final HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException ex) {
            if (ex instanceof InterruptedException) {
                Thread.currentThread().interrupt();
            }
            throw new SupabaseStorageException("Failed to call Supabase Storage delete API.", ex);
        }

        if (response.statusCode() < HttpStatus.OK.value() || response.statusCode() >= HttpStatus.MULTIPLE_CHOICES.value()) {
            throw new SupabaseStorageException("Supabase Storage delete failed (" + response.statusCode() + "): " + response.body());
        }
    }

    private String resolveStorageBaseUrl() {
        String normalized = supabaseUrl.endsWith("/") ? supabaseUrl.substring(0, supabaseUrl.length() - 1) : supabaseUrl;
        if (normalized.endsWith("/storage/v1")) {
            return normalized;
        }
        return normalized + "/storage/v1";
    }

    private String buildDeletePayload(List<String> objectPaths) {
        StringBuilder payload = new StringBuilder("{\"prefixes\":[");
        for (int i = 0; i < objectPaths.size(); i++) {
            if (i > 0) {
                payload.append(',');
            }

            String value = objectPaths.get(i);
            payload.append('"')
                    .append(value.replace("\\", "\\\\").replace("\"", "\\\""))
                    .append('"');
        }
        payload.append("]}");
        return payload.toString();
    }

    private record BucketPath(String bucket, String path) {
    }
}
