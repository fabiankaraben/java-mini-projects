package com.example.plugin.welcome;

import com.example.plugin.api.GreetingExtensionPoint;
import org.pf4j.Extension;

@Extension
public class WelcomeGreeting implements GreetingExtensionPoint {
    @Override
    public String getGreeting() {
        return "Welcome to the Dynamic Plugin System!";
    }
}
