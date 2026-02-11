package com.example.host;

import com.example.plugin.api.GreetingExtensionPoint;
import org.pf4j.PluginManager;
import org.pf4j.PluginWrapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/plugins")
public class PluginController {

    private final PluginManager pluginManager;

    public PluginController(PluginManager pluginManager) {
        this.pluginManager = pluginManager;
    }

    @GetMapping
    public List<Map<String, String>> listPlugins() {
        return pluginManager.getPlugins().stream()
                .map(plugin -> {
                    Map<String, String> map = new HashMap<>();
                    map.put("id", plugin.getPluginId());
                    map.put("state", plugin.getPluginState().toString());
                    map.put("version", plugin.getDescriptor().getVersion());
                    return map;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/greetings")
    public List<String> getGreetings() {
        List<GreetingExtensionPoint> greetings = pluginManager.getExtensions(GreetingExtensionPoint.class);
        return greetings.stream()
                .map(GreetingExtensionPoint::getGreeting)
                .collect(Collectors.toList());
    }
    
    @GetMapping("/load")
    public String loadPlugins() {
        // In a real app, this might point to a specific directory or upload
        // For this demo, we assume plugins are in the 'plugins' directory relative to execution
        pluginManager.loadPlugins();
        pluginManager.startPlugins();
        return "Plugins loaded and started";
    }
}
