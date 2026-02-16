package com.traffgun.acc.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RestController
@RequestMapping("api/ticket-files")
public class TicketFileController {

    private final Path baseDir = Paths.get("ticket_files");
    private final Path commentsDir = Paths.get("ticket_files/comments");

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> getFile(@PathVariable String filename) throws IOException {
        Path file = baseDir.resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }

    @GetMapping("/comments/{filename:.+}")
    public ResponseEntity<Resource> getCommentsFile(@PathVariable String filename) throws IOException {
        Path file = commentsDir.resolve(filename).normalize();
        Resource resource = new UrlResource(file.toUri());
        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        String encodedFileName = URLEncoder.encode(Objects.requireNonNull(resource.getFilename()), StandardCharsets.UTF_8)
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFileName)
                .body(resource);
    }
}
