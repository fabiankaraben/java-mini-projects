package com.example.featureflag;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demo")
public class DemoController {

    private final FeatureFlagService featureFlagService;

    public DemoController(FeatureFlagService featureFlagService) {
        this.featureFlagService = featureFlagService;
    }

    @GetMapping("/message")
    public String getMessage() {
        if (featureFlagService.isEnabled("new-message-feature")) {
            return "Hello from the NEW feature!";
        } else {
            return "Hello from the OLD feature.";
        }
    }
}
