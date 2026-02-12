package com.example.discovery;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/registry")
public class RegistryController {

    private final ServiceRegistry serviceRegistry;

    public RegistryController(ServiceRegistry serviceRegistry) {
        this.serviceRegistry = serviceRegistry;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody ServiceInstance instance) {
        serviceRegistry.register(instance);
        return ResponseEntity.ok("Registered " + instance.getServiceId());
    }

    @PostMapping("/heartbeat")
    public ResponseEntity<String> heartbeat(@RequestParam String serviceId,
                                            @RequestParam String host,
                                            @RequestParam int port) {
        serviceRegistry.renew(serviceId, host, port);
        return ResponseEntity.ok("Heartbeat received");
    }

    @DeleteMapping("/unregister")
    public ResponseEntity<String> unregister(@RequestParam String serviceId,
                                             @RequestParam String host,
                                             @RequestParam int port) {
        serviceRegistry.unregister(serviceId, host, port);
        return ResponseEntity.ok("Unregistered");
    }

    @GetMapping("/instances")
    public List<ServiceInstance> getAllInstances() {
        return serviceRegistry.getAllInstances();
    }

    @GetMapping("/instances/{serviceId}")
    public List<ServiceInstance> getInstancesByServiceId(@PathVariable String serviceId) {
        return serviceRegistry.getInstances(serviceId);
    }
}
