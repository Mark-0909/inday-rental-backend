package com.indayrental.backend.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.ArrayList;
import java.util.List;

@Converter
public class StringListJsonConverter implements AttributeConverter<List<String>, String> {
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        if (attribute == null || attribute.isEmpty()) {
            return "[]";
        }

        StringBuilder builder = new StringBuilder("[");
        for (int i = 0; i < attribute.size(); i++) {
            if (i > 0) {
                builder.append(",");
            }
            String value = attribute.get(i);
            if (value == null) {
                value = "";
            }
            builder.append('"')
                    .append(value.replace("\\", "\\\\").replace("\"", "\\\""))
                    .append('"');
        }
        builder.append(']');
        return builder.toString();
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isBlank() || "[]".equals(dbData.trim())) {
            return new ArrayList<>();
        }

        String trimmed = dbData.trim();
        if (trimmed.startsWith("[") && trimmed.endsWith("]")) {
            trimmed = trimmed.substring(1, trimmed.length() - 1).trim();
        }

        if (trimmed.isEmpty()) {
            return new ArrayList<>();
        }

        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean insideQuotes = false;
        boolean escaping = false;

        for (int i = 0; i < trimmed.length(); i++) {
            char ch = trimmed.charAt(i);

            if (escaping) {
                current.append(ch);
                escaping = false;
                continue;
            }

            if (ch == '\\') {
                current.append(ch);
                escaping = true;
                continue;
            }

            if (ch == '"') {
                insideQuotes = !insideQuotes;
                current.append(ch);
                continue;
            }

            if (ch == ',' && !insideQuotes) {
                values.add(unescapeJsonString(current.toString().trim()));
                current.setLength(0);
                continue;
            }

            current.append(ch);
        }

        if (current.length() > 0 || !trimmed.isEmpty()) {
            values.add(unescapeJsonString(current.toString().trim()));
        }

        return values;
    }

    private String unescapeJsonString(String value) {
        if (value == null) {
            return "";
        }

        String normalized = value.trim();
        if (normalized.startsWith("\"") && normalized.endsWith("\"")) {
            normalized = normalized.substring(1, normalized.length() - 1);
        }

        return normalized
                .replace("\\\"", "\"")
                .replace("\\\\", "\\");
    }
}