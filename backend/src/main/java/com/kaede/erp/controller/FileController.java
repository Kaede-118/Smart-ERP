package com.kaede.erp.controller;


import com.kaede.erp.common.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;


@RestController
@RequestMapping("/api/files")
public class FileController {


    @Value("${app.upload-dir:uploads}")
    private String uploadDir;


    @PostMapping("/upload")
    public Result<String> upload(
            @RequestParam("file") MultipartFile file
    ) throws IOException {


        Path dir = Path.of(uploadDir).toAbsolutePath().normalize();

        if (!Files.exists(dir)) {
            Files.createDirectories(dir);
        }


        String ext = "";

        String originalName = file.getOriginalFilename();

        if (originalName != null && originalName.contains(".")) {
            ext = originalName.substring(originalName.lastIndexOf("."));
        }

        String filename = UUID.randomUUID().toString() + ext;


        file.transferTo(dir.resolve(filename).toFile());


        String url = "/api/files/" + filename;

        return Result.success(url);

    }

}
