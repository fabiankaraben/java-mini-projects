package com.example.plugin.api;

import org.pf4j.ExtensionPoint;

public interface GreetingExtensionPoint extends ExtensionPoint {
    String getGreeting();
}
