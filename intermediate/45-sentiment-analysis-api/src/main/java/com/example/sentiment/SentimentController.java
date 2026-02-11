package com.example.sentiment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sentiment")
public class SentimentController {

    private final SentimentAnalysisService sentimentAnalysisService;

    @Autowired
    public SentimentController(SentimentAnalysisService sentimentAnalysisService) {
        this.sentimentAnalysisService = sentimentAnalysisService;
    }

    @PostMapping("/analyze")
    public SentimentResult analyze(@RequestBody SentimentRequest request) {
        return sentimentAnalysisService.analyze(request.getText());
    }
}
