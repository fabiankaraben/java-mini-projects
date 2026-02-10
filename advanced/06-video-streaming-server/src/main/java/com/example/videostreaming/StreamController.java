package com.example.videostreaming;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/stream")
public class StreamController {

    private final StreamService streamService;

    public StreamController(StreamService streamService) {
        this.streamService = streamService;
    }

    @PostMapping("/start-rtmp")
    public String startRtmp() {
        streamService.startRtmpToHls();
        return "RTMP Listener started. Push stream to rtmp://localhost:1935/live/stream";
    }

    @PostMapping("/start-test")
    public String startTest() {
        streamService.startTestStream();
        return "Test Stream started. Watch at /hls/test.m3u8";
    }
    
    @PostMapping("/stop")
    public String stop() {
        streamService.stopStream();
        return "Stream stopped.";
    }
}
