package com.example.vinha.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("uploads");

    public String storeFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Không thể lưu file rỗng.");
            }

            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            String originalFileName = file.getOriginalFilename();
            if (originalFileName == null) {
                throw new RuntimeException("Tên file không hợp lệ.");
            }
            String safeFileName = originalFileName.replaceAll("[^a-zA-Z0-9._-]", "_");
            String uniqueFileName = UUID.randomUUID().toString() + "_" + safeFileName;

            Path destinationFile = this.rootLocation.resolve(Paths.get(uniqueFileName))
                    .normalize().toAbsolutePath();

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
            }

            return "/uploads/" + uniqueFileName;

        } catch (IOException e) {
            throw new RuntimeException("Lỗi khi lưu file: " + e.getMessage());
        }
    }
}