package com.example.multipart;

import io.javalin.Javalin;
import io.javalin.http.UploadedFile;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {

    private static final String UPLOAD_DIR = "uploads";

    public static void main(String[] args) {
        new App().start(7000);
    }

    public Javalin start(int port) {
        Javalin app = Javalin.create(config -> {
            config.requestLogger.http((ctx, ms) -> {
                System.out.println(ctx.method() + " " + ctx.path() + " - " + ms + "ms");
            });
        }).start(port);

        // Ensure upload directory exists
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        app.post("/upload", ctx -> {
            Map<String, Object> response = new HashMap<>();
            Map<String, String> fields = new HashMap<>();
            List<String> files = new ArrayList<>();

            // Process form fields
            ctx.formParamMap().forEach((key, values) -> {
                fields.put(key, String.join(", ", values));
            });

            // Process uploaded files
            List<UploadedFile> uploadedFiles = ctx.uploadedFiles();
            for (UploadedFile uploadedFile : uploadedFiles) {
                String fileName = uploadedFile.filename();
                Path filePath = Paths.get(UPLOAD_DIR, fileName);
                
                // Save file using standard Java NIO
                Files.copy(uploadedFile.content(), filePath, StandardCopyOption.REPLACE_EXISTING);
                
                files.add(fileName + " (" + uploadedFile.size() + " bytes)");
            }

            response.put("status", "success");
            response.put("message", "Multipart data processed successfully");
            response.put("fields", fields);
            response.put("files", files);

            ctx.json(response);
        });

        System.out.println("Server started at http://localhost:" + app.port() + "/");
        return app;
    }
}
