package com.ffi.api.master.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPOutputStream;

public class GzipUtility {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * M Joko M - 20 Aus 2024
     *
     * Compresses an object to Gzipped JSON and returns a ResponseEntity with proper headers.
     *
     * @param object The object to compress and return.
     * @return ResponseEntity containing the Gzipped JSON response.
     * @throws IOException If an I/O error occurs during compression.
     */
    public static ResponseEntity<byte[]> createGzippedResponse(Object object) throws IOException {
        // Convert the object to JSON string
        String json = objectMapper.writeValueAsString(object);

        // Compress JSON to GZIP
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            gzipOutputStream.write(json.getBytes(StandardCharsets.UTF_8));
        }

        byte[] gzippedData = byteArrayOutputStream.toByteArray();

        // Set headers for GZIP content
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_ENCODING, "gzip");
        headers.add(HttpHeaders.CONTENT_TYPE, "application/json");
        headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(gzippedData.length));

        // Return the Gzipped data as a ResponseEntity
        return new ResponseEntity<>(gzippedData, headers, HttpStatus.OK);
    }
}
