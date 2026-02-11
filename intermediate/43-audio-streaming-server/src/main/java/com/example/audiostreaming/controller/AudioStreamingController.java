package com.example.audiostreaming.controller;

import com.example.audiostreaming.service.AudioService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/audio")
public class AudioStreamingController {

    private final AudioService audioService;

    public AudioStreamingController(AudioService audioService) {
        this.audioService = audioService;
    }

    @GetMapping(value = "/{fileName}", produces = "audio/mpeg")
    public ResponseEntity<Resource> streamAudio(@PathVariable String fileName, @RequestHeader(value = "Range", required = false) String rangeHeader) {
        Resource audio = audioService.loadAudio(fileName);
        
        if (!audio.exists()) {
            return ResponseEntity.notFound().build();
        }

        // Spring Boot's ResourceHttpMessageConverter automatically handles Byte Range requests
        // when we return a Resource in a ResponseEntity.
        // It sets the status to 206 (Partial Content) if a Range header is present,
        // or 200 (OK) if not.
        
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("audio/mpeg"))
                .body(audio);
    }
}
