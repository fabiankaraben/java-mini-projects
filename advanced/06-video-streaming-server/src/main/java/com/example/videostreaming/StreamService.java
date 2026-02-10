package com.example.videostreaming;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class StreamService {

    private static final Logger logger = LoggerFactory.getLogger(StreamService.class);
    private Process ffmpegProcess;

    @Value("${app.hls.dir:hls}")
    private String hlsDir;

    @PostConstruct
    public void init() {
        // Create HLS directory if it doesn't exist
        try {
            Files.createDirectories(Paths.get(hlsDir));
        } catch (IOException e) {
            logger.error("Failed to create HLS directory", e);
        }
    }

    /**
     * Starts an FFmpeg process to listen for RTMP and output HLS.
     * Note: This requires FFmpeg to be installed on the system.
     */
    public void startRtmpToHls() {
        if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
            logger.info("FFmpeg process is already running.");
            return;
        }

        try {
            // Command to listen on RTMP port 1935 and output HLS
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-listen", "1",
                    "-i", "rtmp://0.0.0.0:1935/live/stream",
                    "-c:v", "libx264",
                    "-preset", "veryfast",
                    "-c:a", "aac",
                    "-f", "hls",
                    "-hls_time", "4",
                    "-hls_list_size", "5",
                    "-hls_flags", "delete_segments",
                    hlsDir + "/stream.m3u8"
            );
            
            pb.inheritIO(); // Redirect output to console for debugging
            ffmpegProcess = pb.start();
            logger.info("Started FFmpeg RTMP listener.");
            
        } catch (IOException e) {
            logger.error("Failed to start FFmpeg process", e);
        }
    }
    
    /**
     * Starts a test stream (testsrc) for demonstration purposes if no RTMP source is available.
     */
    public void startTestStream() {
         if (ffmpegProcess != null && ffmpegProcess.isAlive()) {
            logger.info("FFmpeg process is already running.");
            return;
        }

        try {
            // Generates a test pattern
            ProcessBuilder pb = new ProcessBuilder(
                    "ffmpeg",
                    "-re",
                    "-f", "lavfi", "-i", "testsrc=size=640x480:rate=30",
                    "-f", "lavfi", "-i", "sine=frequency=1000",
                    "-c:v", "libx264",
                    "-c:a", "aac",
                    "-f", "hls",
                    "-hls_time", "4",
                    "-hls_list_size", "5",
                    "-hls_flags", "delete_segments",
                    hlsDir + "/test.m3u8"
            );

            pb.inheritIO();
            ffmpegProcess = pb.start();
            logger.info("Started FFmpeg Test Stream.");

        } catch (IOException e) {
            logger.error("Failed to start FFmpeg test stream", e);
        }
    }

    @PreDestroy
    public void stopStream() {
        if (ffmpegProcess != null) {
            ffmpegProcess.destroy();
            logger.info("Stopped FFmpeg process.");
        }
    }
}
