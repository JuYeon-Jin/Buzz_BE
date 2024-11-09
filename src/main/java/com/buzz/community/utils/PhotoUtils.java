package com.buzz.community.utils;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class PhotoUtils {
/*
    public String saveFile(MultipartFile file) {
        String fileName = getFileName(file);
        String uploadDir = "/upload/";
        String savePath = getPath(uploadDir) + fileName;

        try {
            file.transferTo(new File(savePath));
        } catch (IOException e) {
            throw new RuntimeException("Failed to save file", e);
        }

        return uploadDir + fileName;
    }

    private String getFileName(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        return UUID.randomUUID() + extension;
    }

    private String getPath(String uploadDir) {
        Path path = Paths.get(uploadDir);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                throw new RuntimeException("Could not create upload directory", e);
            }
        }
        return path.toString() + File.separator;
    }*/

}
