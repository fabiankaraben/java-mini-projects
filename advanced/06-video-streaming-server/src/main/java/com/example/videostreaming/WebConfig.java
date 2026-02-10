package com.example.videostreaming;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${app.hls.dir:hls}")
    private String hlsDir;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve HLS fragments from the configured local filesystem directory
        String hlsPath = Paths.get(hlsDir).toAbsolutePath().toUri().toString();
        registry.addResourceHandler("/hls/**")
                .addResourceLocations(hlsPath);
    }
}
